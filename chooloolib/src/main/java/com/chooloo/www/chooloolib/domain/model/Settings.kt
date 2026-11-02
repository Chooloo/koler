package com.chooloo.www.chooloolib.domain.model

data class Settings(
    val defaultPage: Page = Page.CONTACTS,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val incomingCallMode: IncomingCallMode = IncomingCallMode.FULL_SCREEN,
    val isAnimationsEnabled: Boolean = true,
    val isGroupRecentsEnabled: Boolean = false,
    val isDialpadTonesEnabled: Boolean = true,
    val isDialpadVibrateEnabled: Boolean = true,
)
