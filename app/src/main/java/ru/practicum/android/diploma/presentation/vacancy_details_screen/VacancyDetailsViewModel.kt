package ru.practicum.android.diploma.presentation.vacancy_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.interactors.VacancyDetailsInteractor
import java.io.IOException

class VacancyDetailsViewModel(
    private val vacancyId: String,
    private val interactor: VacancyDetailsInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<VacancyDetailsUiState>(VacancyDetailsUiState.Loading)
    val uiState: StateFlow<VacancyDetailsUiState> = _uiState

    init {
        loadDetails()
    }

//    fun loadDetails() {
//        _uiState.value = VacancyDetailsUiState.Loading
//
//        viewModelScope.launch {
//            try {
//                val vacancy = interactor.getVacancyDetails(vacancyId)
//                _uiState.value = VacancyDetailsUiState.Content(vacancy)
//            } catch (e: IOException) {
//                _uiState.value = VacancyDetailsUiState.Error(isNetworkError = true)
//            } catch (e: Exception) {
//                _uiState.value = VacancyDetailsUiState.Error(isNetworkError = false)
//            }
//        }
//    }

    fun loadDetails() {
        _uiState.value = VacancyDetailsUiState.Loading

        viewModelScope.launch {
            android.util.Log.d("VM_API", "Запрашиваем детали вакансии через interactor, id=$vacancyId")

            try {
                val vacancy = interactor.getVacancyDetails(vacancyId)

                android.util.Log.d("VM_API", "УСПЕХ: получили VacancyDetails: $vacancy")

                _uiState.value = VacancyDetailsUiState.Content(vacancy)
            } catch (e: IOException) {
                android.util.Log.e("VM_API", "ОШИБКА СЕТИ: ${e.message}")
                _uiState.value = VacancyDetailsUiState.Error(isNetworkError = true)
            } catch (e: Exception) {
                android.util.Log.e("VM_API", "ОШИБКА API/МОДЕЛИ: ${e.message}", e)
                _uiState.value = VacancyDetailsUiState.Error(isNetworkError = false)
            }
        }
    }
}
