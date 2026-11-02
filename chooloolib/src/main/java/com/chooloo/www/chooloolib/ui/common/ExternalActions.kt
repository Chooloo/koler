package com.chooloo.www.chooloolib.ui.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import com.chooloo.www.chooloolib.R

fun Context.openUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

fun Context.openStoreListing() = openUrl("market://details?id=$packageName")

fun Context.sendSupportEmail() = startActivity(
    Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        .putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
)

fun Context.openSms(number: String) =
    startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${PhoneNumberUtils.normalizeNumber(number)}")))

fun Context.openWhatsapp(number: String) = openUrl("https://api.whatsapp.com/send?phone=$number")

fun Context.openManageBlockedNumbers() =
    startActivity(getSystemService(TelecomManager::class.java).createManageBlockedNumbersIntent())

fun Context.createContact(number: String? = null) = startActivity(
    Intent(Intent.ACTION_INSERT)
        .setType(ContactsContract.Contacts.CONTENT_TYPE)
        .apply { number?.let { putExtra(ContactsContract.Intents.Insert.PHONE, it) } }
)

fun Context.editContact(contactId: Long) = startActivity(
    Intent(Intent.ACTION_EDIT, ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId))
)

fun Context.copyToClipboard(text: String) {
    getSystemService(ClipboardManager::class.java).setPrimaryClip(ClipData.newPlainText(text, text))
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        Toast.makeText(this, R.string.number_copied_to_clipboard, Toast.LENGTH_SHORT).show()
    }
}

fun Context.clipboardText(): String? =
    getSystemService(ClipboardManager::class.java).primaryClip?.getItemAt(0)?.text?.toString()
