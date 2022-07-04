package com.chooloo.www.chooloolib.interactor.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.AccentTheme
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.interactor.theme.ThemesInteractor.ThemeMode
import com.chooloo.www.chooloolib.util.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesInteractorImpl @Inject constructor(
    private val prefs: PreferencesManager,
    @ApplicationContext private val context: Context
) : BaseInteractorImpl<PreferencesInteractor.Listener>(),
    PreferencesInteractor {

    override var isAnimations: Boolean
        get() = prefs.getBoolean(
            R.string.pref_key_animations,
            R.bool.pref_default_value_animations
        )
        set(value) {
            prefs.putBoolean(R.string.pref_key_animations, value)
        }

    override var isGroupRecents: Boolean
        get() = prefs.getBoolean(
            R.string.pref_key_group_recents,
            R.bool.pref_default_value_group_recents
        )
        set(value) {
            prefs.putBoolean(R.string.pref_key_group_recents, value)
        }

    override var isDialpadTones: Boolean
        get() = prefs.getBoolean(
            R.string.pref_key_dialpad_tones,
            R.bool.pref_default_value_dialpad_tones
        )
        set(value) {
            prefs.putBoolean(R.string.pref_key_dialpad_tones, value)
        }

    override var isDialpadVibrate: Boolean
        get() = prefs.getBoolean(
            R.string.pref_key_dialpad_vibrate,
            R.bool.pref_default_value_dialpad_vibrate
        )
        set(value) {
            prefs.putBoolean(R.string.pref_key_dialpad_vibrate, value)
        }

    override var defaultPage: Page
        get() = Page.fromKey(prefs.getString(R.string.pref_key_default_page))
        set(value) {
            prefs.putString(R.string.pref_key_default_page, value.key)
        }

    override var themeMode: ThemeMode
        get() = ThemeMode.fromKey(prefs.getString(R.string.pref_key_theme_mode))
        set(value) {
            prefs.putString(R.string.pref_key_theme_mode, value.key)
        }

    override var accentTheme: AccentTheme
        get() = AccentTheme.fromKey(prefs.getString(R.string.pref_key_color))
        set(value) {
            prefs.putString(R.string.pref_key_color, value.key)
        }

    override var incomingCallMode: IncomingCallMode
        get() = IncomingCallMode.fromKey(prefs.getString(R.string.pref_key_incoming_call_mode))
        set(value) {
            prefs.putString(R.string.pref_key_incoming_call_mode, value.key)
        }

    override fun setDefaultValues() {
        PreferenceManager.setDefaultValues(context, R.xml.preferences_chooloo, false)
    }
}