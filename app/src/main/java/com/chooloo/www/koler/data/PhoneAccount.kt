import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.annotation.StringRes
import com.chooloo.www.koler.R

data class PhoneAccount(
    val number: String,
    val contactId: Long,
    val displayName: String,
    val type: PhoneAccountType = PhoneAccountType.OTHER
) {
    enum class PhoneAccountType(val phoneType: Int, @StringRes val stringRes: Int) {
        CAR(Phone.TYPE_CAR, R.string.phone_type_car),
        MMS(Phone.TYPE_MMS, R.string.phone_type_mms),
        HOME(Phone.TYPE_HOME, R.string.phone_type_home),
        WORK(Phone.TYPE_WORK, R.string.phone_type_work),
        ISDN(Phone.TYPE_ISDN, R.string.phone_type_isdn),
        MAIN(Phone.TYPE_MAIN, R.string.phone_type_main),
        RADIO(Phone.TYPE_RADIO, R.string.phone_type_radio),
        TELEX(Phone.TYPE_TELEX, R.string.phone_type_telex),
        PAGER(Phone.TYPE_PAGER, R.string.phone_type_pager),
        OTHER(Phone.TYPE_OTHER, R.string.phone_type_other),
        MOBILE(Phone.TYPE_MOBILE, R.string.phone_type_mobile),
        TTY_TDD(Phone.TYPE_TTY_TDD, R.string.phone_type_tty_ttd),
        FAX_WORK(Phone.TYPE_FAX_WORK, R.string.phone_type_fax_work),
        CALLBACK(Phone.TYPE_CALLBACK, R.string.phone_type_callback),
        OTHER_FAX(Phone.TYPE_OTHER_FAX, R.string.phone_type_other_fax),
        ASSISTANT(Phone.TYPE_ASSISTANT, R.string.phone_type_assistant),
        WORK_PAGER(Phone.TYPE_WORK_PAGER, R.string.phone_type_work_pager),
        WORK_MOBILE(Phone.TYPE_WORK_MOBILE, R.string.phone_type_work_mobile),
        COMPANY_MAIN(Phone.TYPE_COMPANY_MAIN, R.string.phone_type_company_main);

        companion object {
            fun fromType(phoneType: Int) =
                values().associateBy(PhoneAccountType::phoneType).getOrDefault(phoneType, OTHER)
        }
    }
}