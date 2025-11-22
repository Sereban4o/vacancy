package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * Репозиторий для получения тестовых (stub) вакансий.
 * В Issue 4 он уже реализован в Data-слое: StubVacancyRepositoryImpl.
 */
interface StubVacancyRepository {
    suspend fun getStubVacancies(): List<Vacancy>
}
