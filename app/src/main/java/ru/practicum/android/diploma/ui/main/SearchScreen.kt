package ru.practicum.android.diploma.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.components.ChipBox
import ru.practicum.android.diploma.ui.components.InfoState
import ru.practicum.android.diploma.ui.components.SearchInputField
import ru.practicum.android.diploma.ui.components.VacancyItem
import ru.practicum.android.diploma.ui.theme.Spacer12
import ru.practicum.android.diploma.util.TypeState

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onVacancyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState().value

    val pagedData = viewModel.pagingResultDataFlow.collectAsLazyPagingItems()
    val totalFound = viewModel.totalFound.collectAsState().value

    LaunchedEffect(Unit) {
        // Принудительно запускаем обновление при первом рендере (без этого ищет только со 2 раза)
        if (uiState.query.isNotBlank() && pagedData.itemCount == 0) {
            pagedData.retry()
        }
    }

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
        if (uiState.query.isEmpty()) {
            InfoState(TypeState.SearchVacancy)
        } else {
            when (pagedData.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    val error = (pagedData.loadState.refresh as LoadState.Error).error

                    when (error) {
                        is java.io.IOException -> InfoState(TypeState.NoInternet)
                        is retrofit2.HttpException -> InfoState(TypeState.ServerError)
                        else -> {
                            InfoState(TypeState.ServerErrorVacancy)
                        }
                    }
                }

                is LoadState.NotLoading -> {
                    // Заголовок
                    ShowTotalVacancyNumber(uiState, totalFound)
                    // Если вакансий не нашлось
                    if (pagedData.itemCount == 0 && uiState.query.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            InfoState(TypeState.NoDataVacancy)
                        }
                    } else {
                        // Список вакансий
                        CreateVacancyList(pagedData, onVacancyClick)
                    }
                }
            }
        }
    }
}

// Так потом наверное проще будет переделывать
@Composable
private fun ShowTotalVacancyNumber(uiState: SearchUiState, totalFound: Int) {
    // Количество найденных вакансий
    if (!uiState.isInitial) {
        ChipBox(
            text = if (totalFound == 0) {
                stringResource(R.string.no_vacancies)
            } else {
                stringResource(R.string.vacancies_found, uiState.totalFound)
            }
        )
        if (
            !uiState.isLoading &&
            uiState.totalFound == 0 &&
            uiState.errorType == SearchErrorType.NONE
        ) {
            InfoState(TypeState.NoDataVacancy)
        }
    }
}

@Composable
private fun CreateVacancyList(
    pagedData: LazyPagingItems<Vacancy>,
    onVacancyClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = pagedData.itemCount,
            key = { index -> pagedData[index]?.id ?: index }
        ) { index ->
            val vacancy = pagedData[index]
            if (vacancy != null) {
                VacancyItem(
                    vacancy = vacancy,
                    onClick = { onVacancyClick(vacancy.id) }
                )
            }
        }

        if (pagedData.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
