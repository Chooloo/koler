package com.chooloo.www.koler.util

import android.Manifest.permission.WRITE_CONTACTS
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.telephony.PhoneNumberUtils
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.entity.Contact
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions

const val PERMISSION_RC_WRITE_CONTACTS = 1

fun Context.getContactbyId(contactId: Long): Contact {
    PhoneContentResolver(this, contactId).content.also {
        return if (it.isNotEmpty()) it[0] else Contact.UNKNOWN
    }
}

fun Context.lookupContact(number: String): Contact {
    PhoneLookupContentResolver(this, number).content.also {
        return if (it.isNotEmpty()) it[0] else Contact.UNKNOWN
    }
}

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

fun Context.deleteContact(contactId: Long) = runWithPermissions(WRITE_CONTACTS) {
    val uri = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
    contentResolver.delete(uri, null, null)
}

fun Context.smsNumber(number: String?) {
    val uri = Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(number)))
    startActivity(Intent(Intent.ACTION_SENDTO, uri))
}
