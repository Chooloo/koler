package com.chooloo.www.koler.interactor.preferences

import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.AccentTheme
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.RecordFormat
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Sim
import com.chooloo.www.koler.util.PreferencesManager

class PreferencesInteractorImpl(
    private val preferencesManager: PreferencesManager
) : BaseInteractorImpl<PreferencesInteractor.Listener>(),
    PreferencesInteractor {

    override var isRecords: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_records_enabled,
            R.bool.pref_default_value_records_enabled
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_records_enabled, value)
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

    override var isScrollIndicator: Boolean
        get() = preferencesManager.getBoolean(
            R.string.pref_key_scroll_indicator,
            R.bool.pref_default_value_scroll_indicator
        )
        set(value) {
            preferencesManager.putBoolean(R.string.pref_key_scroll_indicator, value)
        }


    override var sim: Sim
        get() = Sim.fromKey(preferencesManager.getString(R.string.pref_key_sim_select))
        set(value) {
            preferencesManager.putString(R.string.pref_key_sim_select, value.key)
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

    override var recordFormat: RecordFormat
        get() = RecordFormat.fromKey(preferencesManager.getString(R.string.pref_key_record_format))
        set(value) {
            preferencesManager.putString(R.string.pref_key_record_format, value.key)
        }
}