package com.chooloo.www.chooloolib.ui.common

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.model.ThemeMode

@get:StringRes
val ThemeMode.titleRes: Int
    get() = when (this) {
        ThemeMode.SYSTEM -> R.string.theme_mode_system
        ThemeMode.LIGHT -> R.string.theme_mode_light
        ThemeMode.DARK -> R.string.theme_mode_dark
        ThemeMode.DYNAMIC -> R.string.theme_mode_dynamic
    }

@get:StringRes
val Page.titleRes: Int
    get() = when (this) {
        Page.DIALER -> R.string.dialer
        Page.CONTACTS -> R.string.contacts
        Page.RECENTS -> R.string.recents
    }

@get:StringRes
val IncomingCallMode.titleRes: Int
    get() = when (this) {
        IncomingCallMode.FULL_SCREEN -> R.string.full_screen
        IncomingCallMode.POP_UP -> R.string.pop_up_notification
    }

@get:StringRes
val ContactAccount.Type.titleRes: Int
    get() = when (this) {
        ContactAccount.Type.IM -> R.string.type_label_im
        ContactAccount.Type.CUSTOM -> R.string.type_label_custom
        ContactAccount.Type.PHONE -> R.string.type_label_phone
        ContactAccount.Type.EMAIL -> R.string.type_label_email
        ContactAccount.Type.NAME -> R.string.type_label_name
        ContactAccount.Type.ORGANIZATION -> R.string.type_label_organization
        ContactAccount.Type.WHATSAPP -> R.string.type_label_whatsapp
    }

@get:StringRes
val Recent.Presentation.fallbackNameRes: Int?
    get() = when (this) {
        Recent.Presentation.ALLOWED -> null
        Recent.Presentation.RESTRICTED -> R.string.caller_restricted
        Recent.Presentation.PAYPHONE -> R.string.caller_payphone
        Recent.Presentation.UNKNOWN -> R.string.caller_unknown
    }
