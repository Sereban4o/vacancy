package ru.practicum.android.diploma.domain.models

/**
 * Универсальный параметр справочника (страна, регион, отрасль и т.п.)
 * Это прямой аналог "parameters(id, name)" из задачи.
 */
data class FilterParameter(
    val id: String,   // ID, который уходит в API
    val name: String, // Человекочитаемое имя, которое показываем в UI
)
