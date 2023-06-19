package com.chooloo.www.chooloolib.interactor.contacts

import android.Manifest.permission.WRITE_CONTACTS
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.Contacts
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.data.repository.contacts.ContactsRepository
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.util.annotation.RequiresDefaultDialer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsInteractorImpl @Inject constructor(
    private val phones: PhonesInteractor,
    private val blocked: BlockedInteractor,
    private val contactsRepository: ContactsRepository,
    @ApplicationContext private val context: Context,
) : BaseInteractorImpl<ContactsInteractor.Listener>(), ContactsInteractor {

    override fun getContact(contactId: Long) = contactsRepository.getContact(contactId)

    override fun getContacts() = contactsRepository.getContacts()

    @RequiresPermission(WRITE_CONTACTS)
    override fun deleteContact(contactId: Long) {
        context.contentResolver.delete(
            Uri.withAppendedPath(
                Contacts.CONTENT_URI,
                contactId.toString()
            ), null, null
        )
    }

    @RequiresDefaultDialer
    override suspend fun blockContact(contactId: Long) {
        val contactAccounts = phones.getContactAccounts(contactId)
        contactAccounts.forEach { blocked.blockNumber(it.number) }
    }

    override suspend fun unblockContact(contactId: Long) {
        val contactAccounts = phones.getContactAccounts(contactId)
        contactAccounts.forEach { blocked.unblockNumber(it.number) }
    }

    @RequiresPermission(WRITE_CONTACTS)
    override fun toggleContactFavorite(contactId: Long, isFavorite: Boolean) {
        val contentValues = ContentValues()
        contentValues.put(Contacts.STARRED, if (isFavorite) 1 else 0)
        val filter = "${Contacts._ID}=$contactId"
        context.contentResolver.update(Contacts.CONTENT_URI, contentValues, filter, null)
    }

    override suspend fun getIsContactBlocked(contactId: Long): Boolean {
        val contactAccounts = phones.getContactAccounts(contactId)
        return contactAccounts.all { blocked.isNumberBlocked(it.number) }
    }
}