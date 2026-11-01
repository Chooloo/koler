package com.chooloo.www.chooloolib.ui.caller

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.domain.model.record.PhoneRecord
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class PhonesListViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val telecomRepository: TelecomRepository,
    private val clipboardRepository: ClipboardRepository
) : RecordsListViewModel<PhoneData, PhoneRecord>() {
    private var _contactId: Long? = null

    fun onContactId(contactId: Long) {
        this._contactId = contactId
        updateItemsFlow()
    }

    fun onItemClick(item: PhoneData) {
        viewModelScope.launch {
            telecomRepository.callNumber(item.number)
        }
    }

    fun onItemLongClick(item: PhoneData) {
        clipboardRepository.copyText(item.number)
    }

    override suspend fun convertRecordToItem(record: PhoneRecord) = PhoneData(
        id = record.id,
        type = record.type,
        label = record.label,
        number = record.number,
        isPrimary = record.isPrimary,
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
