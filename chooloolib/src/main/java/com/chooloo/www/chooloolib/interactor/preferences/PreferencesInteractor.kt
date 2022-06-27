package com.chooloo.www.chooloolib.interactor.preferences

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.interactor.theme.ThemesInteractor.ThemeMode

interface PreferencesInteractor : BaseInteractor<PreferencesInteractor.Listener> {
    interface Listener

    var isAnimations: Boolean
    var isGroupRecents: Boolean
    var isDialpadTones: Boolean
    var isDialpadVibrate: Boolean

    var defaultPage: Page
    var themeMode: ThemeMode
    var accentTheme: AccentTheme

    fun setDefaultValues()

    companion object {
        enum class AccentTheme(val key: String, val theme: Int) {
            RED("red", R.style.Accent_Red),
            BLUE("blue", R.style.Accent_Blue),
            GREEN("green", R.style.Accent_Green),
            PURPLE("purple", R.style.Accent_Purple),
            DEFAULT(BLUE.key, BLUE.theme);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(AccentTheme::key).getOrDefault(key ?: "", DEFAULT)
            }
        }

        enum class Page(val key: String, val index: Int, val titleRes: Int) {
            CONTACTS("contacts", 0, R.string.contacts),
            RECENTS("recents", 1, R.string.recents),
            DEFAULT(CONTACTS.key, CONTACTS.index, R.string.default_page);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(Page::key).getOrDefault(key ?: "", DEFAULT)
            }
        }
    }
}