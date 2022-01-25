package com.chooloo.www.chooloolib.ui.contactsuggestions

import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.adapter.ContactsSuggestionsAdapter
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactory
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.contacts.ContactsController
import javax.inject.Inject

class ContactsSuggestionsController @Inject constructor(
    view: ContactsSuggestionsFragment,
    lifecycleOwner: LifecycleOwner,
    liveDataFactory: LiveDataFactory,
    adapter: ContactsSuggestionsAdapter,
    permissionsInteractor: PermissionsInteractor,
) :
    ContactsController<ContactsSuggestionsFragment>(
        view,
        adapter,
        lifecycleOwner,
        liveDataFactory,
        permissionsInteractor
    ) {
}