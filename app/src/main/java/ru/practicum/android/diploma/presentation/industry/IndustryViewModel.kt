package ru.practicum.android.diploma.presentation.industry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.interactors.IndustriesInteractor
import ru.practicum.android.diploma.domain.models.FilterParameter
import ru.practicum.android.diploma.domain.models.FilterSettings

class IndustryViewModel(
    private val industriesInteractor: IndustriesInteractor,
    private val filterSettingsInteractor: FilterSettingsInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IndustryUiState(isLoading = true))
    val uiState: StateFlow<IndustryUiState> = _uiState.asStateFlow()

    // Полный список отраслей, без поиска
    private var fullList: List<FilterParameter> = emptyList()

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isError = false) }

            try {
                // 1. Загружаем отрасли
                fullList = industriesInteractor.getIndustries()

                // 2. Читаем текущие настройки фильтра
                val currentSettings: FilterSettings = filterSettingsInteractor.getFilterSettings()
                val selectedId = currentSettings.industry?.id

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = false,
                        industries = fullList,       // пока без поиска
                        selectedIndustryId = selectedId
                    )
                }
            } catch (e: Exception) {
                // по ТЗ: показать сообщение об ошибке
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        viewModelScope.launch {
            val trimmed = newQuery.trim()

            // ТЗ: перед поиском рекомендовано очищать выбранную отрасль
            // 1. Фильтруем список
            val filtered = if (trimmed.isEmpty()) {
                fullList
            } else {
                fullList.filter { industry ->
                    industry.name.contains(trimmed, ignoreCase = true)
                }
            }

            // 2. Автовыбор, если осталась ровно ОДНА отрасль
            val autoSelectedId = if (trimmed.isNotEmpty() && filtered.size == 1) {
                filtered.first().id
            } else {
                null
            }

            _uiState.update {
                it.copy(
                    query = trimmed,
                    industries = filtered,
                    selectedIndustryId = autoSelectedId
                )
            }
        }
    }

    fun onIndustryClick(industryId: String) {
        // находим выбранную отрасль в полном списке
        val selected = fullList.firstOrNull { it.id == industryId } ?: return

        _uiState.update {
            it.copy(
                // кладём название выбранной отрасли в поле ввода
                query = selected.name,
                // сужаем список до одной выбранной отрасли
                industries = listOf(selected),
                // помечаем её как выбранную
                selectedIndustryId = industryId
            )
        }
    }

    /**
     * Сохраняем выбранную отрасль в FilterSettings.
     * Возвращаем true, если было что сохранять.
     */
    suspend fun applySelection(): Boolean {
        val state = _uiState.value
        val selectedId = state.selectedIndustryId ?: return false

        val selected = fullList.firstOrNull { it.id == selectedId } ?: return false

        val current = filterSettingsInteractor.getFilterSettings()
        val updated = current.copy(
            industry = FilterParameter(
                id = selected.id,
                name = selected.name
            )
        )
        filterSettingsInteractor.saveFilterSettings(updated)

        // 🔹 очистить поле поиска и восстановить полный список
        _uiState.update {
            it.copy(
                query = "",
                industries = fullList
            )
        }

        return true
    }

}
