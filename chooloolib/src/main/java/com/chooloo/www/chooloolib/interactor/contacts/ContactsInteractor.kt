package com.chooloo.www.chooloolib.interactor.contacts

import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface ContactsInteractor : BaseInteractor<ContactsInteractor.Listener> {
    interface Listener

    fun deleteContact(contactId: Long)
    fun queryContact(contactId: Long, callback: (ContactAccount?) -> Unit)
    fun toggleContactFavorite(contactId: Long, isFavorite: Boolean)
    fun blockContact(contactId: Long, onSuccess: (() -> Unit)? = null)
    fun unblockContact(contactId: Long, onSuccess: (() -> Unit)? = null)
    fun getIsContactBlocked(contactId: Long, callback: (Boolean) -> Unit)
}