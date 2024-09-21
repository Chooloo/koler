package com.chooloo.www.chooloolib.ui.viewmodel.accounts

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.AccountData
import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord
import com.chooloo.www.chooloolib.domain.repository.navigation.NavigationRepository
import com.chooloo.www.chooloolib.domain.repository.rawcontact.RawContactRepository
import com.chooloo.www.chooloolib.ui.viewmodel.accounts.AccountsListViewModel.Errors
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsListViewModelImpl @Inject constructor(
    private val navigationRepository: NavigationRepository,
    private val rawContactRepository: RawContactRepository
) : RecordsListViewModelImpl<AccountData, RawContactRecord>(), AccountsListViewModel {
    private var _contactId: Long? = null

    init {
        updateItemsFlow()
    }

    override suspend fun convertRecordToItem(record: RawContactRecord) = AccountData(
        type = record.type,
        data = record.data,
        contactId = record.contactId,
    )

    override fun getRecordsFlow(filter: String?) =
        _contactId?.let { rawContactRepository.getRawContacts(it, filter) }

    override fun onContactId(contactId: Long) {
        this._contactId = contactId
        updateItemsFlow()
    }

    override fun onItemClick(item: AccountData) {
        if (item.type == RawContactRecord.RawContactType.WHATSAPP) {
            viewModelScope.launch {
                navigationRepository.goToWhatsappNumber(item.data)
            }
        } else {
            onError(Errors.UnknownAccountType())
        }
    }
}