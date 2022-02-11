package com.chooloo.www.chooloolib.interactor.navigation

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_PHONE_STATE
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.model.SimAccount
import com.chooloo.www.chooloolib.ui.base.BaseActivity

interface NavigationsInteractor : BaseInteractor<NavigationsInteractor.Listener> {
    interface Listener

    fun donate()
    fun rateApp()
    fun sendEmail()
    fun reportBug()
    fun goToAppGithub()
    fun manageBlockedNumber()
    fun goToLauncherActivity()
    fun sendSMS(number: String?)
    fun addContact(number: String)
    fun viewContact(contactId: Long)
    fun editContact(contactId: Long)
    fun goToActivity(activityClass: Class<out BaseActivity<*>>)
}