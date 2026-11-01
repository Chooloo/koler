package com.chooloo.www.chooloolib.ui.viewmodel.accounts

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.AccountData
import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord
import com.chooloo.www.chooloolib.domain.repository.navigation.NavigationRepository
import com.chooloo.www.chooloolib.domain.repository.rawcontact.RawContactRepository
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsListViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository,
    private val rawContactRepository: RawContactRepository
) : RecordsListViewModel<AccountData, RawContactRecord>() {
    private var _contactId: Long? = null

    override suspend fun convertRecordToItem(record: RawContactRecord) = AccountData(
        type = record.type,
        data = record.data,
        label = record.label,
        contactId = record.contactId,
        accountName = record.accountName,
        accountType = record.accountType,
    )

    override fun getRecordsFlow(filter: String?) =
        _contactId?.let { rawContactRepository.getRawContacts(it, filter) }

    fun onContactId(contactId: Long) {
        this._contactId = contactId
        updateItemsFlow()
    }

    fun onItemClick(item: AccountData) {
        if (item.type == RawContactRecord.RawContactType.WHATSAPP) {
            viewModelScope.launch {
                navigationRepository.goToWhatsappNumber(item.data)
            }
        } else {
            onError(Errors.UnknownAccountType())
        }
    }

    sealed class Errors {
        class UnknownAccountType : VMError(R.string.error_unknown_account_type)
    }
}
