package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface VacanciesRepository {

    suspend fun searchVacancies(query: String, page: Int, filters: SearchFilters? = null): VacanciesSearchResult
    suspend fun getVacancyDetails(id: String): VacancyDetails
}
