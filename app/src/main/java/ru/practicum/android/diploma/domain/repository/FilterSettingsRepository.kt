package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.FilterSettings

/**
 * Интерфейс репозитория для работы с настройками фильтра.
 * Он живёт в domain-слое и НЕ зависит от способа сериализации (Gson / kotlinx.serialization / что угодно).
 */
interface FilterSettingsRepository {

    /**
     * Прочитать сохранённые настройки фильтра.
     * Если ничего нет, вернуть объект FilterSettings() по умолчанию.
     */
    suspend fun getFilterSettings(): FilterSettings

    /**
     * Сохранить настройки фильтра.
     */
    suspend fun saveFilterSettings(settings: FilterSettings)

    /**
     * Полностью сбросить настройки (как будто фильтр не настроен).
     */
    suspend fun clearFilterSettings()
}
