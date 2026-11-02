package com.chooloo.www.chooloolib.ui.common

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.core.net.toUri
import com.chooloo.www.chooloolib.R

private fun Context.launch(intent: Intent) {
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(this, R.string.error_no_app_found, Toast.LENGTH_SHORT).show()
    }
}

fun Context.openUrl(url: String) = launch(Intent(Intent.ACTION_VIEW, url.toUri()))

fun Context.openStoreListing() = openUrl("market://details?id=$packageName")

fun Context.sendSupportEmail() = launch(
    Intent(Intent.ACTION_SENDTO, "mailto:".toUri())
        .putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
)

fun Context.sendEmail(address: String) = launch(Intent(Intent.ACTION_SENDTO, "mailto:$address".toUri()))

fun Context.openSms(number: String) =
    launch(Intent(Intent.ACTION_SENDTO, "smsto:${PhoneNumberUtils.normalizeNumber(number)}".toUri()))

fun Context.openWhatsapp(number: String) = openUrl("https://api.whatsapp.com/send?phone=$number")

fun Context.openManageBlockedNumbers() =
    launch(getSystemService(TelecomManager::class.java).createManageBlockedNumbersIntent())

fun Context.createContact(number: String? = null) = launch(
    Intent(Intent.ACTION_INSERT)
        .setType(ContactsContract.Contacts.CONTENT_TYPE)
        .apply { number?.let { putExtra(ContactsContract.Intents.Insert.PHONE, it) } }
)

fun Context.editContact(contactId: Long) = launch(
    Intent(Intent.ACTION_EDIT, ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId))
)

fun Context.copyToClipboard(text: String) {
    getSystemService(ClipboardManager::class.java).setPrimaryClip(ClipData.newPlainText(text, text))
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        Toast.makeText(this, R.string.number_copied_to_clipboard, Toast.LENGTH_SHORT).show()
    }
}
