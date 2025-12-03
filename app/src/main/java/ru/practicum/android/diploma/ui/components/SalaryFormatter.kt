package ru.practicum.android.diploma.ui.components

import android.annotation.SuppressLint
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

// Форматтер чисел с разделением на разряды.
// Используем Locale.getDefault(), чтобы не ловить ворнинги про захардкоженную локаль.
@SuppressLint("ConstantLocale")
private val salaryNumberFormat: NumberFormat =
    NumberFormat.getInstance(Locale.getDefault()).apply {
        isGroupingUsed = true // "1000000" -> "1 000 000"
    }

private fun formatNumber(value: Int): String {
    // Некоторые локали используют неразрывный пробел — заменяем на обычный.
    return salaryNumberFormat.format(value).replace('\u00A0', ' ')
}

// Все валюты из стандартной библиотеки: код -> Currency
// Без try/catch, чисто программно.
private val availableCurrencies: Map<String, Currency> =
    Currency.getAvailableCurrencies().associateBy { it.currencyCode.uppercase(Locale.ROOT) }

/**
 * Маппинг кода валюты из API в человекочитаемое отображение для UI.
 *
 * - Для "RUR" делаем спец-кейс → "₽"
 * - Для остальных кодов:
 *      * если это валидный ISO-код, берём Currency и его symbol
 *      * если нет — показываем код как есть
 */
private fun mapCurrencyCodeToDisplay(code: String?): String {
    if (code.isNullOrBlank()) return ""

    val upperCode = code.uppercase(Locale.ROOT)

    // Спец-кейс для рублей из ТЗ/бэка
    if (upperCode == "RUR" || upperCode == "RUB") {
        return "₽"
    }

    val currency = availableCurrencies[upperCode] ?: return code
    // символ по локали устройства (без устаревших конструкторов Locale)
    return currency.symbol
}

/**
 * Форматирование зарплаты по правилам ТЗ:
 * - "от XX", "до XX", "от XX до XX"
 * - "зарплата не указана", если нет значений
 * - валюта отображается всегда (если пришёл код)
 * - числа с разбиением на разряды
 */
fun formatSalary(
    salaryFrom: Int?,
    salaryTo: Int?,
    currencyCode: String?
): String {
    val currency = mapCurrencyCodeToDisplay(currencyCode)

    val fromStr = salaryFrom?.let { formatNumber(it) }
    val toStr = salaryTo?.let { formatNumber(it) }

    return when {
        fromStr != null && toStr != null -> "от $fromStr до $toStr $currency"
        fromStr != null -> "от $fromStr $currency"
        toStr != null -> "до $toStr $currency"
        else -> "зарплата не указана"
    }
}

/**
 * Обёртка для уже существующей доменной модели Vacancy,
 * чтобы не переписывать старый код.
 */
fun formatSalary(vacancy: Vacancy): String {
    return formatSalary(
        salaryFrom = vacancy.salaryFrom,
        salaryTo = vacancy.salaryTo,
        currencyCode = vacancy.currency
    )
}
