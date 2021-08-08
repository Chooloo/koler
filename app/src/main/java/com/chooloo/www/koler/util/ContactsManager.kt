import android.Manifest
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
import android.widget.Toast
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.util.permissions.PermissionsManager
import com.chooloo.www.koler.util.preferences.KolerPreferences

class ContactsManager(private val _context: Context) {
    private val _kolerPreferences by lazy { KolerPreferences(_context) }
    private val _permissionsManager by lazy { PermissionsManager(_context) }


    //region number actions

    fun blockNumber(number: String) {
        if (!queryIsNumberBlocked(number)) {
            val values = ContentValues()
            values.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            _context.contentResolver.insert(
                BlockedNumbers.CONTENT_URI,
                values
            )
        }
    }


    fun unblockNumber(number: String) {
        if (queryIsNumberBlocked(number)) {
            val values = ContentValues()
            values.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            _context.contentResolver.insert(BlockedNumbers.CONTENT_URI, values)?.let {
                _context.contentResolver.delete(it, null, null)
            }
        }
    }

    fun lookupAccountByNumber(number: String?) =
        PhoneLookupContentResolver(_context, number).content.getOrNull(0)

    fun queryIsNumberBlocked(number: String): Boolean {
        return if (_permissionsManager.isDefaultDialer) {
            BlockedNumberContract.isBlocked(_context, number)
        } else {
            _kolerPreferences.apply {
                if (!showedDefaultDialerBlockedNotice) {
                    Toast.makeText(
                        _context,
                        _context.getString(R.string.error_not_default_dialer_blocked),
                        Toast.LENGTH_SHORT
                    ).show()
                    showedDefaultDialerBlockedNotice = true
                }
            }
            false
        }
    }

    //endregion


    //region getters

    fun queryContact(contactId: Long): Contact {
        val contacts = ContactsContentResolver(_context, contactId).content
        return if (contacts.isEmpty()) Contact.UNKNOWN else contacts[0]
    }

    fun queryContactAccounts(contactId: Long) =
        PhoneContentResolver(_context, contactId).content.toTypedArray()

    fun queryIsContactBlocked(contact: Contact) =
        queryContactAccounts(contact.id).all { queryIsNumberBlocked(it.number) }

    fun blockContact(contactId: Long) {
        queryContactAccounts(contactId).forEach { blockNumber(it.number) }
    }

    fun unblockContact(contactId: Long) {
        queryContactAccounts(contactId).forEach { unblockNumber(it.number) }
    }

    //endregion


    //region openers

    fun openSmsView(number: String?) {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.parse(String.format("smsto:%s", PhoneNumberUtils.normalizeNumber(number)))
        )
        _context.startActivity(intent)
    }


    fun openContactView(contactId: Long) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString())
        }
        _context.startActivity(intent)
    }

    fun openAddContactView(number: String) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            type = Contacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
        }
        _context.startActivity(intent)
    }

    fun openEditContactView(contactId: Long) {
        val intent = Intent(Intent.ACTION_EDIT, Contacts.CONTENT_URI).apply {
            data = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId)
        }
        _context.startActivity(intent)
    }

    //endregion


    //region contact actions

    fun deleteContact(contactId: Long) {
        _permissionsManager.runWithPermissions(
            arrayOf(Manifest.permission.WRITE_CONTACTS),
            grantedCallback = {
                _context.contentResolver.delete(
                    Uri.withAppendedPath(Contacts.CONTENT_URI, contactId.toString()),
                    null,
                    null
                )
            })
    }


    fun toggleContactFavorite(contactId: Long, isFavorite: Boolean) {
        _permissionsManager.runWithPermissions(
            arrayOf(Manifest.permission.WRITE_CONTACTS),
            grantedCallback = {
                _context.contentResolver.update(
                    Contacts.CONTENT_URI,
                    ContentValues().apply { put(Contacts.STARRED, if (isFavorite) 1 else 0) },
                    "${Contacts._ID}= $contactId",
                    null
                )
            })
    }

    //endregion
}
