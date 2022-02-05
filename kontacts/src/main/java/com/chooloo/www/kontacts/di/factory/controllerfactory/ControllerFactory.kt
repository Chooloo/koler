package com.chooloo.www.kontacts.di.factory.controllerfactory

import com.chooloo.www.kontacts.ui.main.MainContract
import com.chooloo.www.kontacts.ui.main.MainController

interface ControllerFactory {
    fun getMainController(view:MainContract.View): MainContract.Controller
}