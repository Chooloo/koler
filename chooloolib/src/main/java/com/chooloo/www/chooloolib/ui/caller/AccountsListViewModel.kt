package com.chooloo.www.chooloolib.ui.caller

import com.chooloo.www.chooloolib.domain.model.ContactAccount
import com.chooloo.www.chooloolib.domain.repository.ContactAccountRepository
import com.chooloo.www.chooloolib.ui.common.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountsListViewModel @Inject constructor(
    private val contactAccountRepository: ContactAccountRepository
) : RecordsListViewModel<ContactAccount>() {
    private var _contactId: Long? = null

    fun onContactId(contactId: Long) {
        _contactId = contactId
        updateItemsFlow()
    }

    override fun getItemsFlow(filter: String?) = _contactId?.let(contactAccountRepository::accounts)
}
