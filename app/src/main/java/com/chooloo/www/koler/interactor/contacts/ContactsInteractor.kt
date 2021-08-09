package com.chooloo.www.koler.interactor.contacts

import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.interactor.base.BaseInteractor

open interface ContactsInteractor : BaseInteractor<ContactsInteractor.Listener> {
    interface Listener

    fun getContact(contactId: Long): Contact?
    fun isContactBlocked(contactId: Long): Boolean

    fun deleteContact(contactId: Long)
    fun blockContact(contactId: Long)
    fun unblockContact(contactId: Long)
    fun toggleContactFavorite(contactId: Long, isFavorite: Boolean)
}