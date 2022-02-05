package com.chooloo.www.chooloolib.di.factory.adapter

import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.adapter.RecentsAdapter

interface AdapterFactory {
    fun getPhonesAdapter(): PhonesAdapter
    fun getRecentsAdapter(): RecentsAdapter
    fun getContactsAdapter(): ContactsAdapter
}