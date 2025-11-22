package ru.practicum.android.diploma.ui.navigation

import androidx.navigation.NavController
import ru.practicum.android.diploma.util.Constants.Navigation.VACANCY_DETAILS_ROUTE

/**
 * Навигация на экран деталей вакансии.
 *
 * Контракт:
 * - route: VacancyDetails/{vacancyId}
 * - аргумент: vacancyId (String) — уникальный идентификатор вакансии из API.
 */
fun NavController.navigateToVacancyDetails(vacancyId: String) {
    navigate("$VACANCY_DETAILS_ROUTE/$vacancyId") {
        launchSingleTop = true
    }
}
