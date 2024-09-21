package com.chooloo.www.chooloolib.di.factory.contentresolver

import com.chooloo.www.chooloolib.domain.contentresolver.*

interface ContentResolverFactory {
    fun getPhonesContentResolver(
        filter: String? = null,
        contactId: Long? = null
    ): PhonesContentResolver

    fun getRecentsContentResolver(
        recentId: Long? = null,
        filter: String? = null
    ): RecentsContentResolver

    fun getPhoneLookupContentResolver(
        number: String? = null,
        filter: String? = null
    ): PhoneLookupContentResolver

    fun getRawContactsContentResolver(
        contactId: Long,
        filter: String? = null
    ): RawContactsContentResolver

    fun getContactsContentResolver(
        filter: String? = null,
        contactId: Long? = null
    ): ContactsContentResolver
}