package com.chooloo.www.koler.interactor.contacts

import android.Manifest.permission.WRITE_CONTACTS
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.Contacts
import androidx.annotation.RequiresPermission
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.interactor.PermissionsInteractor
import com.chooloo.www.koler.interactor.numbers.NumbersInteractor
import com.chooloo.www.koler.interactor.phoneaccounts.PhoneAccountsInteractor
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor
import com.chooloo.www.koler.util.permissions.PermissionsManager

class ContactsInteractorImpl(
    val context: Context,
    val numbersInteractor: NumbersInteractor,
    val permissionsManager: PermissionsManager,
    val preferencesInteractor: PreferencesInteractor,
    val permissionsInteractor: PermissionsInteractor,
    val phoneAccountsInteractor: PhoneAccountsInteractor,
) : ContactsInteractor {
    override fun getContact(contactId: Long): Contact? =
        ContactsContentResolver(context, contactId).content.getOrNull(0)

    override fun isContactBlocked(contactId: Long): Boolean =
        phoneAccountsInteractor.getContactAccounts(contactId)
            .all { numbersInteractor.isNumberBlocked(it.number) }


    @RequiresPermission(WRITE_CONTACTS)
    override fun deleteContact(contactId: Long) {
        val uri = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
        context.contentResolver.delete(uri, null, null)
    }


    override fun blockContact(contactId: Long) {
        phoneAccountsInteractor.getContactAccounts(contactId)
            .forEach { numbersInteractor.blockNumber(it.number) }
    }

    override fun unblockContact(contactId: Long) {
        phoneAccountsInteractor.getContactAccounts(contactId)
            .forEach { numbersInteractor.unblockNumber(it.number) }
    }

    @RequiresPermission(WRITE_CONTACTS)
    override fun toggleContactFavorite(contactId: Long, isFavorite: Boolean) {
        val contentValues = ContentValues()
        contentValues.put(Contacts.STARRED, if (isFavorite) 1 else 0)
        val filter = "${Contacts._ID}=$contactId"
        context.contentResolver.update(Contacts.CONTENT_URI, contentValues, filter, null);
    }
}