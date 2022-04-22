package com.chooloo.www.chooloolib.ui.phones

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_PHONE_STATE
import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.livedata.PhonesLiveData
import com.chooloo.www.chooloolib.model.PhoneAccount
import com.chooloo.www.chooloolib.repository.phones.PhonesRepository
import com.chooloo.www.chooloolib.ui.list.ListViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhonesViewState @Inject constructor(
    private val permissions: PermissionsInteractor,
    private val phonesRepository: PhonesRepository,
    private val clipboardManager: ClipboardManager
) :
    ListViewState<PhoneAccount>() {

    override val noResultsIconRes = R.drawable.round_call_24
    override val noResultsTextRes = R.string.error_no_results_phones
    override val noPermissionsTextRes = R.string.error_no_permissions_phones

    val callEvent = DataLiveEvent<String>()
    val contactId = MutableLiveData(0L)

    private val phonesLiveData get() = phonesRepository.getPhones(if (contactId.value == 0L) null else contactId.value) as PhonesLiveData


    override fun onFilterChanged(filter: String?) {
        super.onFilterChanged(filter)
        phonesLiveData.filter = filter
    }

    override fun onItemRightClick(item: PhoneAccount) {
        super.onItemRightClick(item)
        permissions.runWithPermissions(arrayOf(READ_PHONE_STATE, CALL_PHONE), {
            callEvent.call(item.number)
        }, {
            errorEvent.call(R.string.error_no_permissions_make_call)
        })
    }

    override fun onItemLongClick(item: PhoneAccount) {
        clipboardManager.setPrimaryClip(
            ClipData.newPlainText("Copied number", item.number)
        )
        messageEvent.call(R.string.number_copied_to_clipboard)
    }

    override fun getItemsObservable(callback: (LiveData<List<PhoneAccount>>) -> Unit) {
        permissions.runWithReadContactsPermissions {
            onPermissionsChanged(it)
            if (it) callback.invoke(phonesLiveData)
        }
    }

    fun onContactId(contactId: Long) {
        this.contactId.value = contactId
    }
}
