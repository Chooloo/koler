package com.chooloo.www.chooloolib.ui.phones

import android.Manifest.permission.*
import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.model.PhoneAccount
import com.chooloo.www.chooloolib.data.repository.phones.PhonesRepository
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.list.ListViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PhonesViewState @Inject constructor(
    private val permissions: PermissionsInteractor,
    private val phonesRepository: PhonesRepository,
    private val clipboardManager: ClipboardManager
) :
    ListViewState<PhoneAccount>(permissions) {

    override val noResultsIconRes = R.drawable.call
    override val noResultsTextRes = R.string.error_no_results_phones

    override val requiredPermissions = listOf(READ_CONTACTS)

    val callEvent = DataLiveEvent<String>()
    val contactId = MutableLiveData(0L)


    override fun getItemsFlow(filter: String?): Flow<List<PhoneAccount>> =
        phonesRepository.getPhones(if (contactId.value == 0L) null else contactId.value, filter)

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

    fun onContactId(contactId: Long) {
        this.contactId.value = contactId
        updateItemsFlow()
    }
}
