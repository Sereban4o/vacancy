package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.FilterSettings

/**
 * Интерфейс репозитория настроек фильтра.
 * Domain-слой.
 */
interface FilterSettingsRepository {

    suspend fun getFilterSettings(): FilterSettings

    suspend fun saveFilterSettings(settings: FilterSettings)

    suspend fun clearFilterSettings()
}
