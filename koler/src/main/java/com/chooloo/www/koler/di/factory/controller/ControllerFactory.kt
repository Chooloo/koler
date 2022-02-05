package com.chooloo.www.koler.di.factory.controller

import com.chooloo.www.koler.ui.main.MainContract
import com.chooloo.www.koler.ui.settings.SettingsContract

interface ControllerFactory {
    fun getMainController(view: MainContract.View): MainContract.Controller
    fun getSettingsController(view: SettingsContract.View): SettingsContract.Controller
}