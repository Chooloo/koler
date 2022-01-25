package com.chooloo.www.chooloolib.ui.contactsuggestions

import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.adapter.ContactsSuggestionsAdapter
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactory
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.contacts.ContactsController
import javax.inject.Inject

class ContactsSuggestionsController<V : ContactsSuggestionsContract.View> @Inject constructor(
    view: V,
    lifecycleOwner: LifecycleOwner,
    liveDataFactory: LiveDataFactory,
    adapter: ContactsSuggestionsAdapter,
    permissionsInteractor: PermissionsInteractor,
) : ContactsController<V>(
    view,
    adapter,
    lifecycleOwner,
    liveDataFactory,
    permissionsInteractor
), ContactsSuggestionsContract.Controller<V> {
}