package com.chooloo.www.chooloolib.domain.model

import com.chooloo.www.chooloolib.domain.enum.Page
import com.chooloo.www.chooloolib.domain.enum.ThemeMode

data class Settings(
    val defaultPage: Page = Page.CONTACTS,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isGroupRecentsEnabled: Boolean = false,
    val isDialpadTonesEnabled: Boolean = true,
    val isDialpadVibrateEnabled: Boolean = true,
)
