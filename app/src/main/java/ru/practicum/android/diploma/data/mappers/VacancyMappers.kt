package ru.practicum.android.diploma.data.mappers

import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.VacancySearchResponseDto
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Маппер одного VacancyDto в доменную Vacancy.
 */
fun VacancyDetailDto.toDomain(): Vacancy {
    return Vacancy(
        id = id,
        title = name,
        company = employer.name,
        logoUrl = employer.logo,
        salaryFrom = salary?.from,
        salaryTo = salary?.to,
        currency = salary?.currency
    )
}

/**
 * Маппер всего ответа поиска в доменную модель.
 */
fun VacancySearchResponseDto.toDomain(): VacanciesSearchResult {
    return VacanciesSearchResult(
        vacancies = vacancies.map { it.toDomain() },
        page = page,
        pages = pages,
        found = found
    )
}
