package com.chooloo.www.chooloolib.di.factory.contentresolver

import com.chooloo.www.chooloolib.contentresolver.*

interface ContentResolverFactory {
    fun getPhonesContentResolver(contactId: Long? = null): PhonesContentResolver
    fun getRecentsContentResolver(recentId: Long? = null): RecentsContentResolver
    fun getPhoneLookupContentResolver(number: String?): PhoneLookupContentResolver
    fun getRawContactsContentResolver(contactId: Long): RawContactsContentResolver
    fun getContactsContentResolver(contactId: Long? = null): ContactsContentResolver
}