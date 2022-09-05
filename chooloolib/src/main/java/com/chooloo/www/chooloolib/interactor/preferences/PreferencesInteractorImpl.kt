package com.chooloo.www.chooloolib.interactor.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.AccentTheme
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Messager
import com.chooloo.www.chooloolib.interactor.theme.ThemesInteractor.ThemeMode
import com.chooloo.www.chooloolib.util.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) : BaseInteractorImpl<PreferencesInteractor.Listener>(),
    PreferencesInteractor {

    override var isAnimations: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_animations,
            R.bool.pref_default_value_animations
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_animations, value)
        }

    override var isGroupRecents: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_group_recents,
            R.bool.pref_default_value_group_recents
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_group_recents, value)
        }

    override var isDialpadTones: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_dialpad_tones,
            R.bool.pref_default_value_dialpad_tones
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_dialpad_tones, value)
        }

    override var isDialpadVibrate: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_dialpad_vibrate,
            R.bool.pref_default_value_dialpad_vibrate
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_dialpad_vibrate, value)
        }

    override var defaultPage: Page
        get() = Page.fromKey(preferencesManager.getString(R.string.pref_key_default_page))
        set(value) {
            preferencesManager.putString(R.string.pref_key_default_page, value.key)
        }

    override var themeMode: ThemeMode
        get() = ThemeMode.fromKey(preferencesManager.getString(R.string.pref_key_theme_mode))
        set(value) {
            preferencesManager.putString(R.string.pref_key_theme_mode, value.key)
        }

    override var accentTheme: AccentTheme
        get() = AccentTheme.fromKey(preferencesManager.getString(R.string.pref_key_color))
        set(value) {
            preferencesManager.putString(R.string.pref_key_color, value.key)
        }

    override var openMessager: Messager
        get() = Messager.fromKey(preferencesManager.getString(R.string.pref_key_messager))
        set(value) {
            preferencesManager.putString(R.string.pref_key_messager, value.key)
        }

    override fun setDefaultValues() {
        PreferenceManager.setDefaultValues(context, R.xml.preferences_chooloo, false)
    }
}