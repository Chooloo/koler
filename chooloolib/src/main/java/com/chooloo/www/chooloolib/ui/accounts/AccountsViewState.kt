package com.chooloo.www.chooloolib.ui.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.model.RawContactAccount
import com.chooloo.www.chooloolib.repository.rawcontacts.RawContactsRepository
import com.chooloo.www.chooloolib.ui.list.ListViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountsViewState @Inject constructor(
    private val navigations: NavigationsInteractor,
    private val permissions: PermissionsInteractor,
    private val rawContactsRepository: RawContactsRepository
) :
    ListViewState<RawContactAccount>() {

    override val noResultsIconRes = R.drawable.round_call_24
    override val noResultsTextRes = R.string.error_no_results_phones
    override val noPermissionsTextRes = R.string.error_no_permissions_phones

    val contactId = MutableLiveData(0L)
    val callEvent = DataLiveEvent<String>()

    private val accountsLiveData
        get() = contactId.value?.let { rawContactsRepository.getRawContacts(it) }


    override fun onItemRightClick(item: RawContactAccount) {
        super.onItemRightClick(item)
        if (item.type == RawContactAccount.RawContactType.WHATSAPP) {
            navigations.openWhatsapp(item.data)
        }
    }

    override fun getItemsObservable(callback: (LiveData<List<RawContactAccount>>) -> Unit) {
        permissions.runWithReadContactsPermissions {
            onPermissionsChanged(it)
            if (it) accountsLiveData?.let(callback::invoke)
        }
    }

    fun onContactId(contactId: Long) {
        this.contactId.value = contactId
    }
}
