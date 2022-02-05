package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.di.factory.controller.ControllerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.chooloo.www.chooloolib.ui.settings.SettingsFragment as ChoolooSettingsFragment

@AndroidEntryPoint
class SettingsFragment @Inject constructor() : ChoolooSettingsFragment(), SettingsContract.View {
    override val controller by lazy { kolerControllerFactory.getSettingsController(this) }

    @Inject lateinit var kolerControllerFactory: ControllerFactory

    override fun onSetup() {
        super.onSetup()
    }
}