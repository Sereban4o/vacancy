package ru.practicum.android.diploma.util

sealed class Screen(val route: String) {

    data object Main : Screen("main")
    data object Favorites : Screen("favorites")
    data object Team : Screen("team")

    data object WorkPlace : Screen("work_place")
    data object Country : Screen("country")
    data object Region : Screen("region")
    data object Industry : Screen("industry")
    data object Filter : Screen("filter")
    data object VacancyDetails : Screen("vacancy_details")
}
