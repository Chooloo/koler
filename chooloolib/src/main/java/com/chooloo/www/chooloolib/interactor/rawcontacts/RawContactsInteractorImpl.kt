package com.chooloo.www.chooloolib.interactor.rawcontacts

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.RawContactsContentResolver
import com.chooloo.www.chooloolib.interactor.base.BaseInteractorImpl
import com.chooloo.www.chooloolib.model.RawContactAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RawContactsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) :
    BaseInteractorImpl<RawContactsInteractor.Listener>(),
    RawContactsInteractor {

    override fun queryRawContacts(contactId: Long, callback: (List<RawContactAccount>) -> Unit) {
        RawContactsContentResolver(context, contactId).queryItems(callback)
    }
}