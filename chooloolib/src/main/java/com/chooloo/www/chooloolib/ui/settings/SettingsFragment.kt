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
            setMenuResList(menuResList)

            askForColorEvent.observe(this@SettingsFragment) { ev ->
                ev.ifNew?.let { dialogs.askForColor(it, viewState::onColorResponse) }
            }

            askForCompactEvent.observe(this@SettingsFragment) {
                it.ifNew?.let {
                    dialogs.askForBoolean(R.string.hint_compact_mode, viewState::onCompactResponse)
                }
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

    override fun onMenuItemClick(menuItem: MenuItem) {
        viewState.onMenuItemClick(menuItem)
    }
}