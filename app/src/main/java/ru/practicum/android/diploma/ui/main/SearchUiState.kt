package ru.practicum.android.diploma.ui.main

import ru.practicum.android.diploma.domain.models.Vacancy

/**
 * UI-состояние экрана поиска вакансий.
 *
 * @param query текущий текст поисковой строки
 * @param isLoading флаг загрузки (показывать индикатор / скелетон)
 * @param vacancies список найденных вакансий при пагинации больше не нужен, всё хранится в контейнере Paging
 * @param errorMessage текст ошибки (null, если ошибки нет)
 * @param isInitial показывает, что пользователь ещё не запускал поиск
 */
data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val errorType: SearchErrorType = SearchErrorType.NONE,
    val totalFound: Int = 0,
    val isInitial: Boolean = true
)
