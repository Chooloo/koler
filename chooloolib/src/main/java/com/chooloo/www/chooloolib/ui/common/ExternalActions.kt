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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.chooloo.www.chooloolib.R

@Composable
fun rememberExternalActions(): ExternalActions {
    val context = LocalContext.current
    return remember(context) { ExternalActions(context) }
}

class ExternalActions(private val context: Context) {
    fun openUrl(url: String) = launch(Intent(Intent.ACTION_VIEW, url.toUri()))

    fun openStoreListing() = openUrl("market://details?id=${context.packageName}")

    fun sendSupportEmail() = launch(
        Intent(Intent.ACTION_SENDTO, "mailto:".toUri())
            .putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email))),
    )

    fun sendEmail(address: String) = launch(Intent(Intent.ACTION_SENDTO, "mailto:$address".toUri()))

    fun openSms(number: String) = launch(
        Intent(Intent.ACTION_SENDTO, "smsto:${PhoneNumberUtils.normalizeNumber(number)}".toUri()),
    )

    fun openWhatsapp(number: String) = openUrl("https://api.whatsapp.com/send?phone=$number")

    fun openManageBlockedNumbers() = launch(
        context.getSystemService(TelecomManager::class.java).createManageBlockedNumbersIntent(),
    )

    fun createContact(number: String? = null) = launch(
        Intent(Intent.ACTION_INSERT)
            .setType(ContactsContract.Contacts.CONTENT_TYPE)
            .apply { number?.let { putExtra(ContactsContract.Intents.Insert.PHONE, it) } },
    )

    fun editContact(contactId: Long) = launch(
        Intent(
            Intent.ACTION_EDIT,
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId),
        ),
    )

    fun copyToClipboard(text: String) {
        context.getSystemService(ClipboardManager::class.java)
            .setPrimaryClip(ClipData.newPlainText(text, text))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(context, R.string.number_copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launch(intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, R.string.error_no_app_found, Toast.LENGTH_SHORT).show()
        }
    }
}
