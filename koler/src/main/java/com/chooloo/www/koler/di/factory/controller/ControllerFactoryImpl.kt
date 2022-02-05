package com.chooloo.www.koler.di.factory.controller

import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.koler.di.factory.fragment.FragmentFactory
import com.chooloo.www.koler.ui.main.MainContract
import com.chooloo.www.koler.ui.main.MainController
import com.chooloo.www.koler.ui.settings.SettingsContract
import com.chooloo.www.koler.ui.settings.SettingsController
import javax.inject.Inject
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory as ChoolooFragmentsFactory

class ControllerFactoryImpl @Inject constructor(
    private val fragmentFactory: FragmentFactory,
    private val colorsInteractor: ColorsInteractor,
    private val promptsInteractor: PromptsInteractor,
    private val stringsInteractor: StringsInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val navigationsInteractor: NavigationsInteractor,
    private val permissionsInteractor: PermissionsInteractor,
    private val preferencesInteractor: PreferencesInteractor,
    private val choolooFragmentsFactory: ChoolooFragmentsFactory
) : ControllerFactory {
    override fun getMainController(view: MainContract.View): MainContract.Controller {
        return MainController(
            view,
            fragmentFactory,
            promptsInteractor,
            stringsInteractor,
            permissionsInteractor,
            preferencesInteractor,
            choolooFragmentsFactory
        )
    }

    override fun getSettingsController(view: SettingsContract.View): SettingsContract.Controller {
        return SettingsController(
            view,
            colorsInteractor,
            navigationsInteractor,
            dialogsInteractor,
            preferencesInteractor,
            permissionsInteractor
        )
    }
}