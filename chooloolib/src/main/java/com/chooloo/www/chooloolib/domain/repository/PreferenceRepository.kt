package com.chooloo.www.chooloolib.domain.repository

import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface PreferenceRepository {
    val defaultPage: StateFlow<Page>
    val themeMode: StateFlow<ThemeMode>
    val isAnimationsEnabled: StateFlow<Boolean>
    val isGroupRecentsEnabled: StateFlow<Boolean>
    val isDialpadTonesEnabled: StateFlow<Boolean>
    val isDialpadVibrateEnabled: StateFlow<Boolean>
    val incomingCallMode: StateFlow<IncomingCallMode>

    fun setDefaultPage(page: Page)
    fun setThemeMode(themeMode: ThemeMode)
    fun setIsAnimationsEnabled(isEnabled: Boolean)
    fun setIsGroupRecentsEnabled(isEnabled: Boolean)
    fun setIsDialpadTonesEnabled(isEnabled: Boolean)
    fun setIsDialpadVibrateEnabled(isEnabled: Boolean)
    fun setIncomingCallMode(incomingCallMode: IncomingCallMode)
}
