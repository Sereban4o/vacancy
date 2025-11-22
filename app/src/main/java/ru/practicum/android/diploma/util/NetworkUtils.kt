package ru.practicum.android.diploma.util

/**
 * Заглушка утилиты для работы с сетью.
 *
 * Сейчас опирается на константу из [AppConstants.Network],
 * чтобы не держать "true" как магическое значение внутри утилиты.
 */
object NetworkUtils {

    val isOnline: Boolean
        get() = AppConstants.Network.DEFAULT_ONLINE
}
