package ru.practicum.android.diploma.domain.interactors

import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

class SearchVacanciesInteractor(
    private val vacanciesRepository: VacanciesRepository
) {

    suspend fun search(
        query: String,
        page: Int = 0,
        filters: SearchFilters? = null
    ): VacanciesSearchResult {
        return vacanciesRepository.searchVacancies(
            query = query,
            page = page,
            filters = filters
        )
    }
}
