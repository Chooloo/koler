package com.chooloo.www.koler.interactor.contacts

import android.Manifest.permission.WRITE_CONTACTS
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.telephony.PhoneNumberUtils
import androidx.annotation.RequiresPermission
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.interactor.base.BaseInteractorImpl
import com.chooloo.www.koler.interactor.numbers.NumbersInteractor
import com.chooloo.www.koler.interactor.phoneaccounts.PhoneAccountsInteractor

class ContactsInteractorImpl(
    private val context: Context,
    private val numbersInteractor: NumbersInteractor,
    private val phoneAccountsInteractor: PhoneAccountsInteractor,
) : BaseInteractorImpl<ContactsInteractor.Listener>(), ContactsInteractor {
    override fun getContact(contactId: Long): Contact? =
        ContactsContentResolver(context, contactId).content.getOrNull(0)

    override fun isContactBlocked(contactId: Long): Boolean =
        phoneAccountsInteractor.getContactAccounts(contactId)
            .all { numbersInteractor.isNumberBlocked(it.number) }


    @RequiresPermission(WRITE_CONTACTS)
    override fun deleteContact(contactId: Long) {
        ContactsContentResolver(context, contactId).delete()
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


    override fun openSmsView(number: String?) {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(number)))
        )
        context.startActivity(intent)
    }

    override fun openContactView(contactId: Long) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
        }
        context.startActivity(intent)
    }

    override fun openAddContactView(number: String) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
        }
        context.startActivity(intent)
    }

    override fun openEditContactView(contactId: Long) {
        val intent = Intent(Intent.ACTION_EDIT, Contacts.CONTENT_URI).apply {
            data = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId)
        }
        context.startActivity(intent)
    }
}