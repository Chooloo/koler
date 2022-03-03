package com.chooloo.www.koler.ui.settings

import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.chooloo.www.chooloolib.ui.settings.SettingsFragment as ChoolooSettingsFragment

@AndroidEntryPoint
class SettingsFragment @Inject constructor() : ChoolooSettingsFragment() {
    override val viewState: SettingsViewState by activityViewModels()

    override fun onSetup() {
        super.onSetup()

        viewState.apply {
            askForShowBlockedEvent.observe(this@SettingsFragment) {
                it.ifNew?.let {
                    dialogs.askForBoolean(R.string.hint_show_blocked, viewState::onShowBlocked)
                }
            }

            askForDefaultPageEvent.observe(this@SettingsFragment) {
                it.ifNew?.let { dialogs.askForDefaultPage(viewState::onDefaultPageResponse) }
            }

            askForDialpadTonesEvent.observe(this@SettingsFragment) {
                it.ifNew?.let {
                    dialogs.askForBoolean(R.string.hint_dialpad_tones, viewState::onDialpadTones)
                }
            }

            askForDialpadVibrateEvent.observe(this@SettingsFragment) {
                it.ifNew?.let {
                    dialogs.askForBoolean(
                        R.string.hint_dialpad_vibrate,
                        viewState::onDialpadVibrate
                    )
                }
            }
        }
    }
}