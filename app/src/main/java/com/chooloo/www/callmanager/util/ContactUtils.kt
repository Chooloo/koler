package com.chooloo.www.callmanager.util

import android.Manifest.permission
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import com.chooloo.www.callmanager.cursorloader.ContactLookupCursorLoader
import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.ui.base.BaseActivity

object ContactUtils {
    const val PERMISSION_RC_WRITE_CONTACTS = 1

    @JvmStatic
    fun lookupContact(context: Context, phoneNumber: String): Contact {
        return if (PermissionUtils.checkPermission(context, permission.READ_CONTACTS, true)) {
            ContactLookupCursorLoader.lookupContact(context, phoneNumber)
        } else {
            Contact.UNKNOWN
        }
    }

    @JvmStatic
    fun openContact(activity: BaseActivity, contact: Contact) {
        activity.startActivity(Intent(Intent.ACTION_VIEW).apply {
            data = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contact.contactId.toString())
        })
    }

    @JvmStatic
    fun addContact(activity: BaseActivity, contact: Contact) {
        activity.startActivity(Intent(Intent.ACTION_INSERT).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, contact.number)
        })
    }

    @JvmStatic
    fun editContact(activity: BaseActivity, contact: Contact) {
        activity.startActivity(Intent(Intent.ACTION_EDIT, ContactsContract.Contacts.CONTENT_URI).apply {
            data = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact.contactId!!)
        })
    }

    @JvmStatic
    fun deleteContact(activity: BaseActivity, contact: Contact) {
        if (activity.hasPermission(permission.WRITE_CONTACTS)) {
            val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, java.lang.Long.toString(contact.contactId!!))
            activity.contentResolver.delete(uri, null, null)
            activity.showMessage("Contact Deleted")
        } else {
            activity.showError("Give me a permission and try again")
            activity.askForPermission(permission.WRITE_CONTACTS, PERMISSION_RC_WRITE_CONTACTS)
        }
    }

    @JvmStatic
    fun smsContact(activity: Activity, contact: Contact) {
        val uri = Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(contact.number)))
        activity.startActivity(Intent(Intent.ACTION_SENDTO, uri))
    }
}