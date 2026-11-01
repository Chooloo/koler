package com.chooloo.www.chooloolib.domain.repository.navigation

import com.chooloo.www.chooloolib.ui.common.BaseActivity

interface NavigationRepository {
    suspend fun goToDonate()
    suspend fun goToRateApp()
    suspend fun goToAppGithub()
    suspend fun goToSendEmail()
    suspend fun goToReportBug()
    suspend fun goToLauncherActivity()
    suspend fun goToManageBlockedNumbers()
    suspend fun goToSendSms(number: String?)
    suspend fun goToWhatsappNumber(number: String?)
    suspend fun goToActivity(activityClass: Class<out BaseActivity>)
}
