package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.dto.VacancySearchRequestDto
import ru.practicum.android.diploma.data.mappers.toDomain
import ru.practicum.android.diploma.data.network.VacanciesRemoteDataSource
import ru.practicum.android.diploma.domain.models.SearchFilters
import ru.practicum.android.diploma.domain.models.VacanciesSearchResult
import ru.practicum.android.diploma.domain.repository.VacanciesRepository

// требование ТЗ: страница = 20 элементов
private const val VACANCIES_PER_PAGE = 20

class VacanciesRepositoryImpl(
    private val remoteDataSource: VacanciesRemoteDataSource
) : VacanciesRepository {

    override suspend fun searchVacancies(
        query: String,
        page: Int,
        filters: SearchFilters?
    ): VacanciesSearchResult {
        val requestDto = VacancySearchRequestDto(
            text = query,
            page = page,
            perPage = VACANCIES_PER_PAGE,
            salaryFrom = filters?.salaryFrom,
            onlyWithSalary = filters?.onlyWithSalary ?: false,
            regionId = filters?.regionId,
            industryId = filters?.industryId,
        )

        val responseDto = remoteDataSource.searchVacancies(requestDto)
        return responseDto.toDomain()
    }
}
