package com.chooloo.www.chooloolib.domain.repository.preference

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepository
import com.chooloo.www.chooloolib.domain.repository.theme.ThemeRepository.ThemeMode
import kotlinx.coroutines.flow.StateFlow

interface PreferenceRepository : BaseRepository {
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
            @StringRes val descriptionRes: Int? = null
        ) {
            THEME_MODE(R.string.pref_key_theme_mode, R.string.pref_title_theme_mode),
            DEFAULT_PAGE(R.string.pref_key_default_page, R.string.pref_title_default_page),
            ANIMATIONS_ENABLED(R.string.pref_key_animations, R.string.pref_title_animations),
            GROUP_RECENTS_ENABLED(
                R.string.pref_key_group_recents,
                R.string.pref_title_group_recents
            ),
            DIALPAD_TONES_ENABLED(
                R.string.pref_key_dialpad_tones,
                R.string.pref_title_dialpad_tones
            ),
            INCOMING_CALL_MODE(
                R.string.pref_key_incoming_call_mode,
                R.string.pref_title_incoming_call_mode
            ),
            DIALPAD_VIBRATE_ENABLED(
                R.string.pref_key_dialpad_vibrate,
                R.string.pref_title_dialpad_vibrate
            ),
        }

        enum class Page(val key: String, val index: Int, val titleRes: Int) {
            CONTACTS("contacts", 0, R.string.contacts),
            RECENTS("recents", 1, R.string.recents);

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