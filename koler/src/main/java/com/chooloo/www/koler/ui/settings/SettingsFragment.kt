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
            askForDefaultPageEvent.observe(this@SettingsFragment) {
                it.ifNew?.let {
                    dialogs.askForDefaultPage {
                        viewState.onDefaultPageResponse(it)
                        true
                    }
                }
            }

            askForDialpadTonesEvent.observe(this@SettingsFragment) {
                it.ifNew?.let { isActivated ->
                    dialogs.askForBoolean(
                        R.string.hint_dialpad_tones,
                        isActivated
                    ) {
                        viewState.onDialpadTones(it)
                        true
                    }
                }
            }

            askForDialpadVibrateEvent.observe(this@SettingsFragment) {
                it.ifNew?.let { isActivated ->
                    dialogs.askForBoolean(
                        R.string.hint_dialpad_vibrate,
                        isActivated
                    ) {
                        viewState.onDialpadVibrate(it)
                        true
                    }
                }
            }

            askForGroupRecentsEvent.observe(this@SettingsFragment) {
                it.ifNew?.let { isActivated ->
                    dialogs.askForBoolean(
                        R.string.hint_group_recents,
                        isActivated
                    ) {
                        viewState.onGroupRecents(it)
                        true
                    }
                }
            }

            askForIncomingCallModeEvent.observe(this@SettingsFragment) {
                it.ifNew?.let {
                    dialogs.askForIncomingCallMode {
                        viewState.onIncomingCallMode(it)
                        true
                    }
                }
            }
        }
    }
}