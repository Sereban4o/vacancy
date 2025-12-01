package ru.practicum.android.diploma.data.filter

import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.practicum.android.diploma.domain.models.FilterSettings
import androidx.core.content.edit

/**
 * SharedPreferences-слой, но используем kotlinx.serialization вместо Gson.
 */
class FilterPreferencesDataSource(
    private val sharedPreferences: SharedPreferences,
) {

    companion object {
        private const val KEY_FILTER_SETTINGS = "filter_settings_json"
    }

    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true   // важно при обновлении версии
        encodeDefaults = true
    }

    fun readFilterSettings(): FilterSettings? {
        val stored = sharedPreferences.getString(KEY_FILTER_SETTINGS, null)
            ?: return null

        return try {
            json.decodeFromString<FilterSettings>(stored)
        } catch (e: Exception) {
            null
        }
    }

    fun writeFilterSettings(settings: FilterSettings) {
        val encoded = json.encodeToString(settings)
        sharedPreferences.edit() {
            putString(KEY_FILTER_SETTINGS, encoded)
        }
    }

    fun clearFilterSettings() {
        sharedPreferences.edit() {
            remove(KEY_FILTER_SETTINGS)
        }
    }
}
