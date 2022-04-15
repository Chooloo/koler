package com.chooloo.www.chooloolib.interactor.preferences

import androidx.appcompat.app.AppCompatDelegate.*
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface PreferencesInteractor : BaseInteractor<PreferencesInteractor.Listener> {
    interface Listener

    var isCompact: Boolean
    var isAnimations: Boolean
    var isShowBlocked: Boolean
    var isGroupRecents: Boolean
    var isDialpadTones: Boolean
    var isDialpadVibrate: Boolean

    var defaultPage: Page
    var themeMode: ThemeMode
    var accentTheme: AccentTheme


    companion object {
        enum class ThemeMode(val key: String, val titleRes: Int, val mode: Int) {
            DARK("dark", R.string.theme_mode_dark, MODE_NIGHT_YES),
            LIGHT("light", R.string.theme_mode_light, MODE_NIGHT_NO),
            SYSTEM("system", R.string.theme_mode_system, MODE_NIGHT_FOLLOW_SYSTEM);

            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(ThemeMode::key).getOrDefault(key ?: "", SYSTEM)
            }
        }

        enum class AccentTheme(val key: String, val theme: Int) {
            RED("red", R.style.Accent_Red),
            BLUE("blue", R.style.Accent_Blue),
            GREEN("green", R.style.Accent_Green),
            ORANGE("orange", R.style.Accent_Orange),
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