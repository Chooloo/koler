package com.chooloo.www.chooloolib.ui.common

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.enum.Page
import com.chooloo.www.chooloolib.domain.enum.RecentPresentation
import com.chooloo.www.chooloolib.domain.enum.ThemeMode

object Labels {
    @StringRes
    fun titleRes(themeMode: ThemeMode): Int = when (themeMode) {
        ThemeMode.SYSTEM -> R.string.theme_mode_system
        ThemeMode.LIGHT -> R.string.theme_mode_light
        ThemeMode.DARK -> R.string.theme_mode_dark
    }

    @StringRes
    fun titleRes(page: Page): Int = when (page) {
        Page.DIALER -> R.string.dialer
        Page.CONTACTS -> R.string.contacts
        Page.RECENTS -> R.string.recents
    }

    @StringRes
    fun fallbackNameRes(presentation: RecentPresentation): Int? = when (presentation) {
        RecentPresentation.ALLOWED -> null
        RecentPresentation.RESTRICTED -> R.string.caller_restricted
        RecentPresentation.PAYPHONE -> R.string.caller_payphone
        RecentPresentation.UNKNOWN -> R.string.caller_unknown
    }
}
