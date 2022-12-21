package com.chooloo.www.chooloolib.ui.contacts.suggestions

import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.data.repository.contacts.ContactsRepository
import com.chooloo.www.chooloolib.ui.contacts.ContactsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsSuggestionsViewState @Inject constructor(
    permissions: PermissionsInteractor,
    contactsRepository: ContactsRepository
) : ContactsViewState(permissions,contactsRepository)