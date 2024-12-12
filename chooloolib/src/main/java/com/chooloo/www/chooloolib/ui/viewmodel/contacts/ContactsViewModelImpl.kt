package com.chooloo.www.chooloolib.ui.viewmodel.contacts

import android.net.Uri
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class ContactsViewModelImpl @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val contactRepository: ContactRepository
) : RecordsListViewModelImpl<ContactData, ContactRecord>() {
    override suspend fun convertRecordToItem(record: ContactRecord) = ContactData(
        id = record.id,
        name = record.name,
        isFavorite = record.starred,
        imageUri = record.photoUri?.let { Uri.parse(record.photoUri) }
    )

    override fun getRecordsFlow(filter: String?): Flow<List<ContactRecord>> =
        contactRepository.getContactsFlow(filter)

    override suspend fun enrichItem(item: ContactData): ContactData {
        phoneRepository.getContactAccounts(item.id).firstOrNull()
            ?.let { item.number = it.number }
        return item
    }
}

