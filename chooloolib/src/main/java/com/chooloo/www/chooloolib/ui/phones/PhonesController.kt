package com.chooloo.www.chooloolib.ui.phones

import android.content.ClipData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.ui.list.ListController

class PhonesController<V : PhonesContract.View>(view: V) :
    ListController<PhoneAccount, V>(view),
    PhonesContract.Controller<V> {

    override val adapter by lazy { PhonesAdapter(component) }

    private val phonesLiveData by lazy {
        component.liveDataFactory.allocPhonesProviderLiveData(if (view.contactId == 0L) null else view.contactId)
    }


    override val noResultsIconRes = R.drawable.ic_call_black_24dp
    override val noResultsTextRes = R.string.error_no_results_phones
    override val noPermissionsTextRes = R.string.error_no_permissions_phones


    override fun applyFilter(filter: String) {
        super.applyFilter(filter)
        phonesLiveData.filter = filter
    }

    override fun onItemClick(item: PhoneAccount) {
        component.navigations.call(item.number)
    }

    override fun onItemLongClick(item: PhoneAccount) {
        component.clipboardManager.setPrimaryClip(
            ClipData.newPlainText("Copied number", item.number)
        )
        view.showMessage(R.string.number_copied_to_clipboard)
    }

    override fun fetchData(callback: (items: List<PhoneAccount>, hasPermissions: Boolean) -> Unit) {
        component.permissions.runWithReadContactsPermissions {
            if (it) {
                phonesLiveData.observe(component.lifecycleOwner) { data ->
                    callback.invoke(data, true)
                }
            } else {
                callback.invoke(emptyList(), false)
            }
        }
    }
}
