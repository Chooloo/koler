package com.chooloo.www.chooloolib.ui.contacts

import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.repository.contacts.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsSuggestionsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    contactsRepository: ContactsRepository
) : ContactsViewState(contactsRepository, permissions) {
}