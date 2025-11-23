package ru.practicum.android.diploma.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R

/**
 * Картинка логотипа работодателя.
 * Загружается по URL, с плейсхолдером и fallback.
 */
@Composable
fun CompanyLogo(
    logoUrl: String?,
    modifier: Modifier = Modifier
) {
    val placeholder = painterResource(R.drawable.ic_company_placeholder)

    AsyncImage(
        model = logoUrl,
        contentDescription = null,
        modifier = modifier,
        placeholder = placeholder, // пока грузится
        error = placeholder,       // если ошибка
        fallback = placeholder,    // если url == null
        contentScale = ContentScale.Crop
    )
}
