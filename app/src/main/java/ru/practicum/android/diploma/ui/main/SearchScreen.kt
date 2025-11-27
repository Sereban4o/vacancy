package ru.practicum.android.diploma.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.components.ChipBox
import ru.practicum.android.diploma.ui.components.ContentPlaceholder
import ru.practicum.android.diploma.ui.components.SearchInputField
import ru.practicum.android.diploma.ui.components.VacancyItem
import ru.practicum.android.diploma.ui.theme.Spacer12

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onVacancyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState().value

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле ввода текста
        SearchInputField(
            query = uiState.query,
            onTextChanged = viewModel::onQueryChanged,
            onClearClick = { viewModel.onQueryChanged("") }
        )
        Spacer(modifier = Modifier.height(Spacer12))
        // Ошибка
        if (uiState.errorType != SearchErrorType.NONE) {
            when (uiState.errorType) {
                SearchErrorType.NETWORK -> {
                    ContentPlaceholder(
                        imageRes = R.drawable.ic_no_internet,
                        headlineText = stringResource(R.string.network_error_headline)
                    )
                }
                SearchErrorType.GENERAL -> {
                    ContentPlaceholder(
                        imageRes = R.drawable.ic_no_found,
                        headlineText = stringResource(R.string.no_vacancies_error_headline)
                    )
                }
                else -> {}
            }
            Text(
                text = when (uiState.errorType) {
                    SearchErrorType.NETWORK ->
                        stringResource(R.string.error_network)
                    SearchErrorType.GENERAL ->
                        stringResource(R.string.error_general)
                    else -> ""
                },
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Количество найденных вакансий
        if (!uiState.isInitial) {
            ChipBox(
                text = if (uiState.totalFound == 0) {
                    stringResource(R.string.no_vacancies)
                } else {
                    stringResource(R.string.vacancies_found, uiState.totalFound)
                }
            )
        } else {
            ContentPlaceholder(
                imageRes = R.drawable.ic_no_content,
            )
        }

        // Список вакансий
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = uiState.vacancies,
                key = { it.id }
            ) { vacancy ->
                VacancyItem(
                    vacancy = vacancy,
                    onClick = { onVacancyClick(vacancy.id) }
                )
            }
        }
    }
}
