package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.ui.base.menu.BaseMenuFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class SettingsFragment @Inject constructor() : BaseMenuFragment() {
    override val viewState: SettingsViewState by viewModels()

    @Inject lateinit var dialogs: DialogsInteractor


    override fun onSetup() {
        super.onSetup()
        viewState.apply {
            askForColorEvent.observe(this@SettingsFragment) { ev ->
                ev.ifNew?.let { dialogs.askForColor(it, viewState::onColorResponse) }
            }

            askForThemeModeEvent.observe(this@SettingsFragment) {
                it.ifNew?.let { dialogs.askForThemeMode(viewState::onThemeModeResponse) }
            }

            askForAnimationsEvent.observe(this@SettingsFragment) {
                it.ifNew?.let {
                    dialogs.askForBoolean(R.string.hint_animations, viewState::onAnimationsResponse)
                }
            }
        }
    }
}