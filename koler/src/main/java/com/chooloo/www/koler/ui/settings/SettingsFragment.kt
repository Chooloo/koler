package com.chooloo.www.koler.ui.settings

import androidx.fragment.app.activityViewModels
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
                it.contentIfNew?.let { dialogs.askForShowBlock(viewState::onShowBlockedResponse) }
            }

            askForDefaultPageEvent.observe(this@SettingsFragment) {
                it.contentIfNew?.let { dialogs.askForDefaultPage(viewState::onDefaultPageResponse) }
            }

            askForShouldAsmSimEvent.observe(this@SettingsFragment) {
                it.contentIfNew?.let { dialogs.askForShouldAskSim(viewState::onShouldAskSimResponse) }
            }
        }
    }
}