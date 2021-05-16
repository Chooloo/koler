package com.chooloo.www.koler.util

import android.Manifest.permission.WRITE_CONTACTS
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.provider.BlockedNumberContract
import android.provider.BlockedNumberContract.BlockedNumbers
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.util.permissions.isDefaultDialer
import com.chooloo.www.koler.util.permissions.runWithPermissions
import com.chooloo.www.koler.util.preferences.KolerPreferences

fun Context.lookupContactNumber(number: String?) =
    PhoneLookupContentResolver(this, number).content.getOrNull(0)

fun Context.lookupContactId(contactId: Long): Contact {
    val contacts = ContactsContentResolver(this, contactId).content.contacts
    return if (contacts.isEmpty()) {
        Contact.UNKNOWN
    } else {
        contacts[0].apply {
            phoneAccounts =
                PhoneContentResolver(
                    this@lookupContactId,
                    contactId
                ).content.phoneAccounts.toTypedArray()
        }
    }
}

fun Context.openContact(contactId: Long) {
    val intent = Intent(ACTION_VIEW)
    intent.data = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
    startActivity(intent)
}

fun Context.addContact(number: String) {
    val intent = Intent(ACTION_INSERT).apply {
        type = Contacts.CONTENT_TYPE
        putExtra(ContactsContract.Intents.Insert.PHONE, number)
    }
    startActivity(intent)
}

fun Context.editContact(contactId: Long) {
    val intent = Intent(ACTION_EDIT, Contacts.CONTENT_URI)
    intent.data = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId)
    startActivity(intent)
}

fun Activity.deleteContact(contactId: Long) {
    runWithPermissions(arrayOf(WRITE_CONTACTS), grantedCallback = {
        contentResolver.delete(
            Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString()),
            null,
            null
        )
    })
}

fun Context.smsNumber(number: String?) {
    val intent = Intent(
        ACTION_SENDTO,
        Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(number)))
    )
    startActivity(intent)
}

fun Activity.setContactFavorite(contactId: Long, isFavorite: Boolean) =
    runWithPermissions(arrayOf(WRITE_CONTACTS), grantedCallback = {
        contentResolver.update(
            Contacts.CONTENT_URI,
            ContentValues().apply { put(Contacts.STARRED, if (isFavorite) 1 else 0) },
            "${Contacts._ID}= $contactId",
            null
        )
    })

fun Context.isNumberBlocked(number: String) =
    if (isDefaultDialer()) {
        BlockedNumberContract.isBlocked(this, number)
    } else {
        KolerPreferences(this).apply {
            if (!showedDefaultDialerBlockedNotice) {
                Toast.makeText(
                    this@isNumberBlocked,
                    getString(R.string.error_not_default_dialer_blocked),
                    Toast.LENGTH_SHORT
                ).show()
                showedDefaultDialerBlockedNotice = true
            }
        }
        false
    }

fun Context.isContactBlocked(contact: Contact) =
    contact.phoneAccounts.all { isNumberBlocked(it.number) }

fun Context.blockNumber(number: String) {
    if (!isNumberBlocked(number)) {
        val values = ContentValues()
        values.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        contentResolver.insert(BlockedNumbers.CONTENT_URI, values)
    }
}

fun Context.unblockNumber(number: String) {
    if (isNumberBlocked(number)) {
        val values = ContentValues()
        values.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        contentResolver.insert(BlockedNumbers.CONTENT_URI, values)?.let {
            contentResolver.delete(it, null, null)
        }
    }
}