package ru.practicum.android.diploma.domain.models

/**
 * Единый контракт фильтров для поискового запроса вакансий.
 *
 * @param salaryFrom нижняя граница зарплаты (в целевой валюте API)
 * @param salaryTo верхняя граница зарплаты
 * @param onlyWithSalary только вакансии с указанной зарплатой
 * @param regionId идентификатор региона (как его понимает API)
 * @param remoteOnly только удалённая работа (если поддерживается API)
 */
data class SearchFilters(
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val onlyWithSalary: Boolean = false,
    val regionId: String? = null,
    val remoteOnly: Boolean? = null
    // добавишь сюда остальные фильтры, когда команда утвердит финальный список
)
