package ru.practicum.android.diploma.ui.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.ActionIcon
import ru.practicum.android.diploma.ui.theme.PrimaryLight
import ru.practicum.android.diploma.ui.theme.SearchFieldBackgroundDark
import ru.practicum.android.diploma.ui.theme.SearchFieldBackgroundLight
import ru.practicum.android.diploma.ui.theme.SearchFieldTextColor
import ru.practicum.android.diploma.ui.theme.TextColorDark

@Composable
fun ExpectedSalaryField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()

    val fieldBackground = if (isDark) {
        SearchFieldBackgroundDark // ночь: #AEAFB4
    } else {
        SearchFieldBackgroundLight // день: #E6E8EB
    }

    val placeholderColor = if (isDark) {
        TextColorDark // ночь: белый
    } else {
        SearchFieldBackgroundDark // день: #AEAFB4
    }

    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(51.dp) // высота по макету
            .background(
                color = fieldBackground,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = { new ->
                if (new.all { it.isDigit() }) onValueChange(new)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = SearchFieldTextColor
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary), // синий курсор
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state -> isFocused = state.isFocused },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // верхняя строка — заголовок
                        Text(
                            text = stringResource(R.string.expected_salary),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isFocused || value.isNotEmpty())
                                PrimaryLight
                            else
                                placeholderColor
                        )

                        // плейсхолдер + текстовое поле в одной области
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (value.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.salary_hint),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = placeholderColor
                                )
                            }
                            innerTextField() // гарантированная ширина → курсор виден при фокусе
                        }
                    }

                    if (value.isNotEmpty()) {
                        Spacer(Modifier.height(0.dp))
                        ActionIcon(
                            iconRes = R.drawable.ic_clear_24,
                            onClick = onClear,
                            tint = SearchFieldTextColor
                        )
                    }
                }
            }
        )
    }
}
