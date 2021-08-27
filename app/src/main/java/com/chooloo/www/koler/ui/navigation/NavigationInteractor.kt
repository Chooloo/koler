package com.chooloo.www.koler.ui.navigation

import com.chooloo.www.koler.interactor.base.BaseInteractor

interface NavigationInteractor : BaseInteractor<NavigationInteractor.Listener> {
    interface Listener

    fun goToRateApp()
    fun goToSendEmail()
    fun goToAppGithub()
    fun goToDonatePage()
    fun goToMainActivity()
    fun goToReportBugPage()
    fun goToManageBlockedNumbers()
}