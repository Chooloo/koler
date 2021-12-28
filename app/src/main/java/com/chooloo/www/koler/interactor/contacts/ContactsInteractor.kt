package com.chooloo.www.koler.interactor.contacts

import com.chooloo.www.koler.data.account.ContactAccount
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface ContactsInteractor : BaseInteractor<ContactsInteractor.Listener> {
    interface Listener

    fun deleteContact(contactId: Long)
    fun queryContact(contactId: Long, callback: (ContactAccount?) -> Unit)
    fun toggleContactFavorite(contactId: Long, isFavorite: Boolean)
    fun blockContact(contactId: Long, onSuccess: (() -> Unit)? = null)
    fun unblockContact(contactId: Long, onSuccess: (() -> Unit)? = null)
    fun getIsContactBlocked(contactId: Long, callback: (Boolean) -> Unit)
}