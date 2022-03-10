package com.chooloo.www.kontacts.di.factory.fragment

import com.chooloo.www.kontacts.ui.contact.ContactFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FragmentFactoryImpl @Inject constructor() : FragmentFactory {
    override fun getContactFragment(contactId: Long) = ContactFragment.newInstance(contactId)
}