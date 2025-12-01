package ru.practicum.android.diploma.domain.interactors

import ru.practicum.android.diploma.domain.models.FilterSettings
import ru.practicum.android.diploma.domain.repositories.FilterSettingsRepository

/**
 * Интерактор, который будет использоваться во ViewModel экрана фильтра и поиска.
 */
class FilterSettingsInteractor(
    private val repository: FilterSettingsRepository
) {

    suspend fun getFilterSettings(): FilterSettings {
        return repository.getFilterSettings()
    }

    suspend fun saveFilterSettings(settings: FilterSettings) {
        repository.saveFilterSettings(settings)
    }

    suspend fun clearFilterSettings() {
        repository.clearFilterSettings()
    }
}
