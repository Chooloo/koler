package com.chooloo.www.chooloolib.interactor.theme

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface ThemesInteractor : BaseInteractor<ThemesInteractor.Listener> {
    interface Listener {}

    fun applyTheme()
}