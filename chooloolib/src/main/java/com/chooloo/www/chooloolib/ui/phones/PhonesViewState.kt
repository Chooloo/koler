package com.chooloo.www.chooloolib.ui.phones

import android.Manifest.permission.*
import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.model.PhoneAccount
import com.chooloo.www.chooloolib.data.repository.phones.PhonesRepository
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.list.ListViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
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

    private val _contactId = MutableLiveData(0L)
    private val _callEvent = MutableDataLiveEvent<String>()

    val contactId = _contactId as LiveData<Long>
    val callEvent = _callEvent as DataLiveEvent<String>


    override fun getItemsFlow(filter: String?): Flow<List<PhoneAccount>> =
        phonesRepository.getPhones(if (contactId.value == 0L) null else contactId.value, filter)

    override fun onItemRightClick(item: PhoneAccount) {
        super.onItemRightClick(item)
        permissions.runWithPermissions(arrayOf(READ_PHONE_STATE, CALL_PHONE), {
            _callEvent.call(item.number)
        }, {
            onError(R.string.error_no_permissions_make_call)
        })
    }

    override fun onItemLongClick(item: PhoneAccount) {
        clipboardManager.setPrimaryClip(
            ClipData.newPlainText("Copied number", item.number)
        )
        onMessage(R.string.number_copied_to_clipboard)
    }

    fun onContactId(contactId: Long) {
        _contactId.value = contactId
        updateItemsFlow()
    }
}
