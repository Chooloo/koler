package com.chooloo.www.chooloolib.ui.viewmodel.accounts

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.model.AccountData
import com.chooloo.www.chooloolib.domain.model.record.RawContactRecord
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModel

interface AccountsListViewModel : RecordsListViewModel<AccountData, RawContactRecord>,
    BaseViewModel {

    fun onContactId(contactId: Long)
    fun onItemClick(item: AccountData)

    sealed class Errors {
        class UnknownAccountType : BaseViewModel.VMError(R.string.error_unknown_account_type)
    }
}