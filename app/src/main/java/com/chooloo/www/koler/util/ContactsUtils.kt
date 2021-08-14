import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.telephony.PhoneNumberUtils

object ContactsUtils {
    fun openSmsView(context: Context, number: String?) {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(number)))
        )
        context.startActivity(intent)
    }

    fun openContactView(context: Context, contactId: Long) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
        }
        context.startActivity(intent)
    }

    fun openAddContactView(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
        }
        context.startActivity(intent)
    }

    fun openEditContactView(context: Context, contactId: Long) {
        val intent = Intent(Intent.ACTION_EDIT, Contacts.CONTENT_URI).apply {
            data = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId)
        }
        context.startActivity(intent)
    }
}
