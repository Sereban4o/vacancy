package ru.practicum.android.diploma.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.ui.components.SearchInputField
import ru.practicum.android.diploma.ui.components.VacancyItem

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onVacancyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState().value

    val pagedData = viewModel.pagingResultDataFlow.collectAsLazyPagingItems()
    val totalFound = viewModel.totalFound.collectAsState().value

    // Обработка состояний загрузки
    LaunchedEffect(pagedData.loadState){
        when(val refreshState = pagedData.loadState.refresh){
            is LoadState.Loading ->{
                // Происходит загрузка данных
            }
            is LoadState.NotLoading -> {
                // Всё загружено
            }
            is LoadState.Error -> {
                // Какая-то ошибка
            }

        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Поле ввода текста
        SearchInputField(
            query = uiState.query,
            onTextChanged = viewModel::onQueryChanged,
            onClearClick = { viewModel.onQueryChanged("") }
        )

        // Ошибка
        if (uiState.errorType != SearchErrorType.NONE) {
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
            Text(
                text = "Найдено $totalFound вакансий",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }

        // Список вакансий (обновлён для пагинации)
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = pagedData.itemCount, // Сколько всего элементов
                key = { index -> pagedData[index]?.id ?: index } // Для оптимизации
            ) { vacancyIndex -> // Индекс текущего элемента
                val vacancy = pagedData[vacancyIndex]
                if(vacancy != null){
                    VacancyItem(
                        vacancy = vacancy,
                        onClick = {onVacancyClick(vacancy.id)}
                    )
                }
            }

            // Индикатор загрузки для следующих страниц, временно так
            if(pagedData.loadState.append is LoadState.Loading){
                item{
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
