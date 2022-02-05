package com.chooloo.www.kontacts.di.factory.controllerfactory

import com.chooloo.www.kontacts.ui.main.MainContract
import com.chooloo.www.kontacts.ui.main.MainController
import javax.inject.Inject

class ControllerFactoryImpl @Inject constructor() : ControllerFactory {
    override fun getMainController(view: MainContract.View): MainContract.Controller {
        return MainController(view)
    }
}