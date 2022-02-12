package com.chooloo.www.chooloolib.ui.contacts

import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class ContactsFragment @Inject constructor() :
    ListFragment<ContactAccount, ContactsViewState>() {

    @Inject override lateinit var adapter: ContactsAdapter
    override val viewState: ContactsViewState by activityViewModels()
}