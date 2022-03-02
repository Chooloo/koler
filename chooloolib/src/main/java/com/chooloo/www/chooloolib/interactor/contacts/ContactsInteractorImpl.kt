package com.chooloo.www.chooloolib.interactor.contacts

import android.Manifest.permission.WRITE_CONTACTS
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.Contacts
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.contentresolver.ContactsContentResolver
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.util.annotation.RequiresDefaultDialer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val blockedInteractor: BlockedInteractor,
    private val phonesInteractor: PhonesInteractor,
) : BaseInteractorImpl<ContactsInteractor.Listener>(), ContactsInteractor {

    override fun queryContact(contactId: Long, callback: (ContactAccount?) -> Unit) {
        ContactsContentResolver(context, contactId).queryItems { contacts ->
            contacts.let { callback.invoke(contacts.getOrNull(0)) }
        }
    }


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
    override fun blockContact(contactId: Long, onSuccess: (() -> Unit)?) {
        phonesInteractor.getContactAccounts(contactId) { accounts ->
            accounts?.forEach { blockedInteractor.blockNumber(it.number) }
            onSuccess?.invoke()
        }
    }

    override fun unblockContact(contactId: Long, onSuccess: (() -> Unit)?) {
        phonesInteractor.getContactAccounts(contactId) { accounts ->
            accounts?.forEach { blockedInteractor.unblockNumber(it.number) }
            onSuccess?.invoke()
        }
    }

    @RequiresPermission(WRITE_CONTACTS)
    override fun toggleContactFavorite(contactId: Long, isFavorite: Boolean) {
        val contentValues = ContentValues()
        contentValues.put(Contacts.STARRED, if (isFavorite) 1 else 0)
        val filter = "${Contacts._ID}=$contactId"
        context.contentResolver.update(Contacts.CONTENT_URI, contentValues, filter, null)
    }

    override fun getIsContactBlocked(contactId: Long, callback: (Boolean) -> Unit) {
        phonesInteractor.getContactAccounts(contactId) { accounts ->
            callback.invoke(accounts?.all { blockedInteractor.isNumberBlocked(it.number) } ?: false)
        }
    }
}