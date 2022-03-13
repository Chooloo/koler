package com.chooloo.www.chooloolib.ui.contacts

import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class ContactsFragment @Inject constructor() :
    ListFragment<ContactAccount, ContactsViewState>() {
    override val viewState: ContactsViewState by activityViewModels()

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject override lateinit var adapter: ContactsAdapter


    override fun onSetup() {
        super.onSetup()

        viewState.showContactEvent.observe(this@ContactsFragment) { ev ->
            ev.ifNew?.let {
                prompts.showFragment(fragmentFactory.getBriefContactFragment(it))
            }
        }
    }
}