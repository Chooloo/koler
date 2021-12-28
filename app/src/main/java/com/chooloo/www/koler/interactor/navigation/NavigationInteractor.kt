package com.chooloo.www.koler.interactor.navigation

import com.chooloo.www.koler.data.account.SimAccount
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface NavigationInteractor : BaseInteractor<NavigationInteractor.Listener> {
    interface Listener

    fun donate()
    fun rateApp()
    fun sendEmail()
    fun reportBug()
    fun goToAppGithub()
    fun goToMainActivity()
    fun manageBlockedNumber()
    fun sendSMS(number: String?)
    fun addContact(number: String)
    fun viewContact(contactId: Long)
    fun editContact(contactId: Long)

    fun callVoicemail()
    fun call(number: String)
    fun call(simAccount: SimAccount?, number: String)
}