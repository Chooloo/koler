package com.chooloo.www.chooloolib.ui.contacts

import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import com.chooloo.www.chooloolib.ui.common.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : RecordsListViewModel<Contact>() {
    override fun getItemsFlow(filter: String?) = contactRepository.contacts(filter)
}
