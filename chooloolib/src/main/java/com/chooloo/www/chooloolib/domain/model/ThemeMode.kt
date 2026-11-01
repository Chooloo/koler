package com.chooloo.www.chooloolib.domain.model

import com.chooloo.www.chooloolib.R

enum class ThemeMode(val key: String, val titleRes: Int) {
    DARK("dark", R.string.theme_mode_dark),
    LIGHT("light", R.string.theme_mode_light),
    SYSTEM("system", R.string.theme_mode_system),
    DYNAMIC("dynamic", R.string.theme_mode_dynamic);

    companion object {
        fun fromKey(key: String?) =
            entries.associateBy(ThemeMode::key).getOrDefault(key ?: "", SYSTEM)
    }
}
