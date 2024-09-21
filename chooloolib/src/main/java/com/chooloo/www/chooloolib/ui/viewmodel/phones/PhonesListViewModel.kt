package com.chooloo.www.chooloolib.ui.viewmodel.phones

import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.domain.model.record.PhoneRecord
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModel

interface PhonesListViewModel : RecordsListViewModel<PhoneData, PhoneRecord> {
    fun onContactId(contactId: Long)
    fun onItemClick(item: PhoneData)
    fun onItemLongClick(item: PhoneData)
}