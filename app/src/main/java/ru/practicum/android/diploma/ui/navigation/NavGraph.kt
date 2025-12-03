package ru.practicum.android.diploma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.presentation.favorites.FavoritesViewModel
import ru.practicum.android.diploma.presentation.vacancydetails.VacancyDetailsViewModel
import ru.practicum.android.diploma.presentation.vacancydetails.VacancyDetailsViewModel.Companion.ARG_FROM_API
import ru.practicum.android.diploma.presentation.vacancydetails.VacancyDetailsViewModel.Companion.ARG_VACANCY_ID
import ru.practicum.android.diploma.ui.filter.country.CountryScreen
import ru.practicum.android.diploma.ui.details.VacancyDetailsScreen
import ru.practicum.android.diploma.ui.favorites.FavouritesScreen
import ru.practicum.android.diploma.ui.filter.FilterSettingsScreen
import ru.practicum.android.diploma.ui.filter.industry.IndustryScreen
import ru.practicum.android.diploma.ui.main.MainScreen
import ru.practicum.android.diploma.ui.filter.region.RegionScreen
import ru.practicum.android.diploma.ui.team.TeamScreen
import ru.practicum.android.diploma.ui.filter.workplace.WorkPlaceScreen
import ru.practicum.android.diploma.util.Screen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Main.route,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 🟦 Главный экран
        composable(Screen.Main.route) {
            MainScreen(
                onFilterClick = { navHostController.navigate(Screen.Filter.route) },
//                onFilterClick = {
//                    // ⬇️ ВРЕМЕННО тест на экран место работы
//                    navHostController.navigate(Screen.WorkPlace.route)
//                },
                onVacancyClick = { id ->
                    navHostController.navigateToVacancyDetails(id, true)
                }
            )
        }

        // Фильтр 🔹
        composable(Screen.Filter.route) {
            FilterSettingsScreen(
                onBack = { navHostController.popBackStack() },
                onWorkPlaceClick = {
                    navHostController.navigate(Screen.WorkPlace.route)
                },
                onIndustryClick = {
                    navHostController.navigate(Screen.Industry.route)
                }
            )
        }

        // Страна 🔹
        composable(Screen.Country.route) {
            CountryScreen(
                onBack = { navHostController.popBackStack() },
            )
        }

        // Регион 🔹
        composable(Screen.Region.route) {
            RegionScreen(
                onBack = { navHostController.popBackStack() }
            )
        }

        // Отрасли 🔹
        composable(Screen.Industry.route) {
            IndustryScreen(
                onBack = { navHostController.popBackStack() }
            )
        }

        // Место работы 🔹
        composable(Screen.WorkPlace.route) {
            WorkPlaceScreen(
                onBack = { navHostController.popBackStack() },
                onCountryClick = {
                    navHostController.navigate(Screen.Country.route)
                },
                onRegionClick = {
                    navHostController.navigate(Screen.Region.route)
                }
            )
        }

        // ⭐ Избранное
        composable(Screen.Favorites.route) {
            val vm: FavoritesViewModel = koinViewModel()
            FavouritesScreen(
                viewModel = vm,
                onVacancyClick = { id ->
                    navHostController.navigateToVacancyDetails(id, false)
                }
            )
        }

        // 👥 Команда
        composable(Screen.Team.route) {
            TeamScreen()
        }

        // 📄 Детали вакансии
        composable(
            route = "${Screen.VacancyDetails.route}/{$ARG_VACANCY_ID}?$ARG_FROM_API={$ARG_FROM_API}",
            arguments = listOf(
                navArgument(ARG_VACANCY_ID) { type = NavType.StringType },
                navArgument(ARG_FROM_API) {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) {
            val vm: VacancyDetailsViewModel = koinViewModel()
            VacancyDetailsScreen(
                onBack = { navHostController.popBackStack() },
                viewModel = vm
            )
        }
    }
}
