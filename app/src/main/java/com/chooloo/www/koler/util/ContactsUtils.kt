package com.chooloo.www.koler.util

import android.Manifest.permission.WRITE_CONTACTS
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.BlockedNumberContract
import android.provider.BlockedNumberContract.BlockedNumbers
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.telephony.PhoneNumberUtils
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.util.permissions.runWithPermissions

fun Context.lookupContactNumber(number: String?) =
    PhoneLookupContentResolver(this, number).content.getOrNull(0)

fun Context.lookupContactId(contactId: Long): Contact {
    val contacts = ContactsContentResolver(this, contactId).content.contacts
    if (contacts.isEmpty()) {
        return Contact.UNKNOWN
    }
    return contacts[0].apply {
        phoneAccounts =
            PhoneContentResolver(this@lookupContactId, contactId).content.phoneAccounts
    }
}

fun Context.lookupPhoneAccounts(contactId: Long) =
    PhoneContentResolver(this, contactId).content

fun Context.openContact(contactId: Long) {
    startActivity(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
    })
}

fun Context.addContact(number: String) {
    startActivity(Intent(Intent.ACTION_INSERT).apply {
        type = Contacts.CONTENT_TYPE
        putExtra(ContactsContract.Intents.Insert.PHONE, number)
    })
}

fun Context.editContact(contactId: Long) {
    startActivity(Intent(Intent.ACTION_EDIT, Contacts.CONTENT_URI).apply {
        data = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId)
    })
}

fun Activity.deleteContact(contactId: Long) =
    runWithPermissions(arrayOf(WRITE_CONTACTS), grantedCallback = {
        val uri = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
        contentResolver.delete(uri, null, null)
    })

fun Context.smsNumber(number: String?) {
    val uri = Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(number)))
    startActivity(Intent(Intent.ACTION_SENDTO, uri))
}

fun Activity.setFavorite(contactId: Long, isFavorite: Boolean) =
    runWithPermissions(arrayOf(WRITE_CONTACTS), grantedCallback = {
        contentResolver.update(
            Contacts.CONTENT_URI,
            ContentValues().apply { put(Contacts.STARRED, if (isFavorite) 1 else 0) },
            "${Contacts._ID}= $contactId",
            null
        )
    })

fun Context.isNumberBlocked(number: String) = BlockedNumberContract.isBlocked(this, number)

fun Context.isContactBlocked(contact: Contact) =
    contact.phoneAccounts.all { isNumberBlocked(it.number) }

fun Context.blockNumber(number: String) {
    if (!isNumberBlocked(number)) {
        val values = ContentValues().apply { put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number) }
        contentResolver.insert(BlockedNumbers.CONTENT_URI, values)
    }
}

fun Context.unblockNumber(number: String) {
    if (isNumberBlocked(number)) {
        val values = ContentValues().apply { put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number) }
        val uri = contentResolver.insert(BlockedNumbers.CONTENT_URI, values)
        uri?.let { contentResolver.delete(it, null, null) }
    }
}