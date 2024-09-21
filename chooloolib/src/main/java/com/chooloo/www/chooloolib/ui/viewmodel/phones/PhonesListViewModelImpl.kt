package com.chooloo.www.chooloolib.ui.viewmodel.phones

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.domain.model.record.PhoneRecord
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhonesListViewModelImpl @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val telecomRepository: TelecomRepository,
    private val clipboardRepository: ClipboardRepository
) : RecordsListViewModelImpl<PhoneData, PhoneRecord>(), PhonesListViewModel {
    private var _contactId: Long? = null

    init {
        updateItemsFlow()
    }

    override fun onContactId(contactId: Long) {
        this._contactId = contactId
        updateItemsFlow()
    }

    override fun onItemClick(item: PhoneData) {
        viewModelScope.launch {
            telecomRepository.callNumber(item.number)
        }
    }

    override fun onItemLongClick(item: PhoneData) {
        clipboardRepository.copyText(item.number)
    }

    override suspend fun convertRecordToItem(record: PhoneRecord) = PhoneData(
        type = record.type,
        label = record.label,
        number = record.number,
        normalizedNumber = record.normalizedNumber
    )

    override fun getRecordsFlow(filter: String?): Flow<List<PhoneRecord>>? =
        _contactId?.let {
            phoneRepository.getPhonesFlow(
                if (_contactId == 0L) null else _contactId,
                filter
            )
        }
}