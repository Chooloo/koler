package com.chooloo.www.chooloolib.di.factory.contentresolver

import com.chooloo.www.chooloolib.contentresolver.ContactsContentResolver
import com.chooloo.www.chooloolib.contentresolver.PhoneLookupContentResolver
import com.chooloo.www.chooloolib.contentresolver.PhonesContentResolver
import com.chooloo.www.chooloolib.contentresolver.RecentsContentResolver

interface ContentResolverFactory {
    fun getPhonesContentResolver(contactId: Long? = null): PhonesContentResolver
    fun getRecentsContentResolver(recentId: Long? = null): RecentsContentResolver
    fun getPhoneLookupContentResolver(number: String?): PhoneLookupContentResolver
    fun getContactsContentResolver(contactId: Long? = null): ContactsContentResolver
}