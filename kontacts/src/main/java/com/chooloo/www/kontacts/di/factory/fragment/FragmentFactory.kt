package com.chooloo.www.kontacts.di.factory.fragment

import com.chooloo.www.kontacts.ui.contact.ContactFragment

interface FragmentFactory {
    fun getContactFragment(contactId: Long): ContactFragment
}