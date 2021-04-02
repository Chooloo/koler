import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.annotation.StringRes
import com.chooloo.www.koler.R

data class PhoneAccount(
    val number: String,
    val contactId: Long,
    val displayName: String,
    val normalizedNumber: String,
    val type: Int = Phone.TYPE_OTHER
)
