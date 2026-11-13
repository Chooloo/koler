package com.chooloo.www.chooloolib.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferenceRepository {
    override val settings: Flow<Settings> = dataStore.data
        .map { preferences ->
            val defaults = Settings()
            Settings(
                defaultPage = preferences.enum(DEFAULT_PAGE, PAGE_KEYS) ?: defaults.defaultPage,
                themeMode = preferences.enum(THEME_MODE, THEME_MODE_KEYS) ?: defaults.themeMode,
                isGroupRecentsEnabled = preferences[GROUP_RECENTS] ?: defaults.isGroupRecentsEnabled,
                isDialpadTonesEnabled = preferences[DIALPAD_TONES] ?: defaults.isDialpadTonesEnabled,
                isDialpadVibrateEnabled = preferences[DIALPAD_VIBRATE] ?: defaults.isDialpadVibrateEnabled,
            )
        }
        .distinctUntilChanged()

    override suspend fun setDefaultPage(page: Page) = set(DEFAULT_PAGE, PAGE_KEYS.getValue(page))

    override suspend fun setThemeMode(themeMode: ThemeMode) = set(THEME_MODE, THEME_MODE_KEYS.getValue(themeMode))

    override suspend fun setIsGroupRecentsEnabled(isEnabled: Boolean) = set(GROUP_RECENTS, isEnabled)

    override suspend fun setIsDialpadTonesEnabled(isEnabled: Boolean) = set(DIALPAD_TONES, isEnabled)

    override suspend fun setIsDialpadVibrateEnabled(isEnabled: Boolean) = set(DIALPAD_VIBRATE, isEnabled)

    private suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    private fun <T : Enum<T>> Preferences.enum(key: Preferences.Key<String>, keys: Map<T, String>): T? =
        this[key]?.let { stored -> keys.entries.firstOrNull { it.value == stored }?.key }

    private companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DEFAULT_PAGE = stringPreferencesKey("default_page")
        val GROUP_RECENTS = booleanPreferencesKey("group_recents")
        val DIALPAD_TONES = booleanPreferencesKey("dialpad_tones")
        val DIALPAD_VIBRATE = booleanPreferencesKey("dialpad_vibrate")

        val PAGE_KEYS = mapOf(Page.DIALER to "dialer", Page.CONTACTS to "contacts", Page.RECENTS to "recents")
        val THEME_MODE_KEYS = mapOf(
            ThemeMode.SYSTEM to "system",
            ThemeMode.LIGHT to "light",
            ThemeMode.DARK to "dark",
        )
    }
}
