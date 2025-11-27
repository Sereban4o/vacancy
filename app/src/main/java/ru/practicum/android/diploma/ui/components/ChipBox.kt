package ru.practicum.android.diploma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.ui.theme.BoxBackground
import ru.practicum.android.diploma.ui.theme.CornerRadiusMedium
import ru.practicum.android.diploma.ui.theme.Padding12
import ru.practicum.android.diploma.ui.theme.Padding4

@Composable
fun ChipBox(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .background(
                color = BoxBackground,
                shape = RoundedCornerShape(CornerRadiusMedium)
            )
            .padding(horizontal = Padding12, vertical = Padding4),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun ChipBoxPreview() {
    ChipBox(
        text = "Таких вакансий нет"
    )
}
