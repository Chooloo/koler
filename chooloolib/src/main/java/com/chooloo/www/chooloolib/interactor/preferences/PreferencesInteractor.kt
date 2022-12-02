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
    var openMessager: Messager

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

        enum class Messager(val key: String, val url: String, val index: Int, val titleRes: Int){
            WHATSAPP("whatsapp", "http://api.whatsapp.com/send?phone=", 0, R.string.whatsapp),
            TELEGRAM("telegram", "https://t.me/", 1, R.string.telegram),
            SIGNAL("signal","https://signal.me/#p/",2,R.string.signal);
            //THREEMA("threema","https://threema.id/compose?text=''&id=",3, R.string.threema);
            // threema need the id from the contact list not working currently
            companion object {
                fun fromKey(key: String?) =
                    values().associateBy(Messager::key).getOrDefault(key ?: "", WHATSAPP)
            }
        }
    }
}