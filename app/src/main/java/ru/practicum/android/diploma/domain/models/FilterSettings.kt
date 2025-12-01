package ru.practicum.android.diploma.domain.models

/**
 * Настройки фильтра поиска вакансий.
 * Это прямой аналог "filter(...)" из задачи, но расширенный под ТЗ.
 *
 * ТЗ требует хранить:
 * - Уровень ЗП
 * - Не показывать без ЗП
 * - Отрасль
 * - Страна
 * - Регион
 */
data class FilterSettings(
    // "Уровень ЗП" (может быть не задан)
    val salaryFrom: Int? = null,

    // "Не показывать без зарплаты"
    val withSalaryOnly: Boolean = false,

    // "Отрасль" (sector в описании задачи)
    val industry: FilterParameter? = null,

    // "Страна" (country)
    val country: FilterParameter? = null,

    // "Регион" (region)
    val region: FilterParameter? = null,
)
