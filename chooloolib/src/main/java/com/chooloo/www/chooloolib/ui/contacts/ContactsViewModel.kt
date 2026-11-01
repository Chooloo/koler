package com.chooloo.www.chooloolib.ui.contacts

import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.ui.common.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val contactRepository: ContactRepository
) : RecordsListViewModel<ContactData, ContactRecord>() {
    override suspend fun convertRecordToItem(record: ContactRecord) =
        ContactData.fromRecord(record)

    override fun getRecordsFlow(filter: String?): Flow<List<ContactRecord>> =
        contactRepository.getContactsFlow(filter)

    override suspend fun enrichItem(item: ContactData): ContactData {
        if (!item.hasPhoneNumber) return item
        val number = phoneRepository.getContactAccounts(item.id).firstOrNull()?.number
        return if (number != null) item.copy(number = number) else item
    }
}
