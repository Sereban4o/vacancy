package ru.practicum.android.diploma.domain.interactors

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

class SearchVacanciesInteractorImpl(
    private val vacanciesRepository: VacanciesRepository
) : SearchVacanciesInteractor{

    // Теперь используем этот метод для поиска
    override suspend fun searchPaged(
        query: String,
        filters: SearchFilters?,
        onTotalFound : (Int) -> Unit
    ): Flow<PagingData<Vacancy>> {
        return vacanciesRepository.searchVacanciesPaged(
            query = query,
            filters = filters,
            onTotalFound = onTotalFound
        )
    }
}
