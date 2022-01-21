package com.chooloo.www.chooloolib.interactor.preferences

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.AccentTheme
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.util.PreferencesManager

class PreferencesInteractorImpl(
    private val preferencesManager: PreferencesManager
) : BaseInteractorImpl<PreferencesInteractor.Listener>(),
    PreferencesInteractor {
    override var isAskSim: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_ask_sim,
            R.bool.pref_default_value_should_ask_sim
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_ask_sim, value)
        }

    override var isCompact: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_compact,
            R.bool.pref_default_value_compact
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_compact, value)
        }

    override var isAnimations: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_animations,
            R.bool.pref_default_value_animations
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_animations, value)
        }

    override var defaultPage: Page
        get() = Page.fromKey(preferencesManager.getString(R.string.pref_key_default_page))
        set(value) {
            preferencesManager.putString(R.string.pref_key_default_page, value.key)
        }

    override var accentTheme: AccentTheme
        get() = AccentTheme.fromKey(preferencesManager.getString(R.string.pref_key_color))
        set(value) {
            preferencesManager.putString(R.string.pref_key_color, value.key)
        }
}