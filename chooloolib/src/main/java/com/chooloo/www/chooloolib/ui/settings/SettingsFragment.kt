package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseMenuFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class SettingsFragment @Inject constructor() : BaseMenuFragment() {
    override val title by lazy { getString(R.string.settings) }
    override val viewState: SettingsViewState by viewModels()

    @Inject lateinit var dialogs: DialogsInteractor


    override fun onSetup() {
        super.onSetup()
        viewState.apply {
            menuResList.observe(this@SettingsFragment, this@SettingsFragment::setMenuResList)

            askForColorEvent.observe(this@SettingsFragment) { ev ->
                ev.contentIfNew?.let { dialogs.askForColor(it, viewState::onColorResponse) }
            }

            askForCompactEvent.observe(this@SettingsFragment) {
                it.contentIfNew?.let { dialogs.askForCompact(viewState::onCompactResponse) }
            }

            askForAnimationsEvent.observe(this@SettingsFragment) {
                it.contentIfNew?.let { dialogs.askForAnimations(viewState::onAnimationsResponse) }
            }
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        viewState.onMenuItemClick(menuItem)
    }
}