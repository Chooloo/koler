package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.Settings
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    val settings: Flow<Settings>

    suspend fun setDefaultPage(page: Page)
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setIsGroupRecentsEnabled(isEnabled: Boolean)
    suspend fun setIsDialpadTonesEnabled(isEnabled: Boolean)
    suspend fun setIsDialpadVibrateEnabled(isEnabled: Boolean)
}
