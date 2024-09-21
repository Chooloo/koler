package com.chooloo.www.chooloolib.domain.repository.theme

import androidx.appcompat.app.AppCompatDelegate
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository

interface ThemeRepository : BaseRepository {
    fun applyThemeMode(themeMode: ThemeMode)

    enum class ThemeMode(val key: String, val titleRes: Int, val mode: Int) {
        DARK("dark", R.string.theme_mode_dark, AppCompatDelegate.MODE_NIGHT_YES),
        LIGHT("light", R.string.theme_mode_light, AppCompatDelegate.MODE_NIGHT_NO),
        SYSTEM("system", R.string.theme_mode_system, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
        DYNAMIC("dynamic", R.string.theme_mode_dynamic, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        companion object {
            fun fromKey(key: String?) =
                entries.associateBy(ThemeMode::key).getOrDefault(key ?: "", SYSTEM)
        }
    }
}