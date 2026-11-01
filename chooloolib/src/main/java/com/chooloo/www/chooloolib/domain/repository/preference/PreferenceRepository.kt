package com.chooloo.www.chooloolib.domain.repository.preference

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R
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
    fun setIncomingCallModel(incomingCallMode: IncomingCallMode)

    companion object {
        enum class ChoolooPreference(
            @StringRes val key: Int,
            @StringRes val titleRes: Int,
            @StringRes val descriptionRes: Int? = null,
            val isDialerOnly: Boolean = false
        ) {
            THEME_MODE(R.string.pref_key_theme_mode, R.string.pref_title_theme_mode),
            ANIMATIONS_ENABLED(R.string.pref_key_animations, R.string.pref_title_animations),
            DEFAULT_PAGE(
                R.string.pref_key_default_page,
                R.string.pref_title_default_page,
                isDialerOnly = true
            ),
            INCOMING_CALL_MODE(
                R.string.pref_key_incoming_call_mode,
                R.string.pref_title_incoming_call_mode,
                isDialerOnly = true
            ),
            GROUP_RECENTS_ENABLED(
                R.string.pref_key_group_recents,
                R.string.pref_title_group_recents,
                isDialerOnly = true
            ),
            DIALPAD_TONES_ENABLED(
                R.string.pref_key_dialpad_tones,
                R.string.pref_title_dialpad_tones,
                isDialerOnly = true
            ),
            DIALPAD_VIBRATE_ENABLED(
                R.string.pref_key_dialpad_vibrate,
                R.string.pref_title_dialpad_vibrate,
                isDialerOnly = true
            ),
            MANAGE_BLOCKED(
                R.string.pref_key_manage_blocked,
                R.string.pref_title_manage_blocked,
                isDialerOnly = true
            ),
            CLEAR_RECENTS(
                R.string.pref_key_clear_recents,
                R.string.action_clear_recents,
                descriptionRes = R.string.explain_clear_recents,
                isDialerOnly = true
            ),
            RATE(R.string.pref_key_rate, R.string.pref_title_rate),
            EMAIL(R.string.pref_key_email, R.string.pref_title_email),
            REPORT_BUGS(R.string.pref_key_report_bugs, R.string.pref_title_report_bugs),
            DONATE(R.string.pref_key_donate, R.string.pref_title_donate),
        }

        enum class Page(val key: String, val index: Int, val titleRes: Int) {
            CONTACTS("contacts", 1, R.string.contacts),
            RECENTS("recents", 2, R.string.recents),
            DIALER("dialer", 0, R.string.dialer);

            companion object {
                fun fromKey(key: String?) =
                    entries.associateBy(Page::key).getOrDefault(key ?: "", CONTACTS)
            }
        }

        enum class IncomingCallMode(val key: String, val index: Int, val titleRes: Int) {
            POP_UP("popup_notification", 0, R.string.pop_up_notification),
            FULL_SCREEN("full_screen", 1, R.string.full_screen);

            companion object {
                fun fromKey(key: String?) =
                    entries.associateBy(IncomingCallMode::key)
                        .getOrDefault(key ?: "", FULL_SCREEN)
            }
        }
    }
}
