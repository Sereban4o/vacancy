package ru.practicum.android.diploma.ui.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.collectAsState().value

    val errorText: String? = when (state.errorType) {
        SearchErrorType.NONE -> null
        SearchErrorType.NETWORK -> stringResource(R.string.error_network)
        SearchErrorType.GENERAL -> stringResource(R.string.error_general)
    }

    if (errorText != null) {
        Text(
            text = errorText,
            color = MaterialTheme.colorScheme.error,
        )
    }

    // дальше — поле поиска, список и т.д.
}

