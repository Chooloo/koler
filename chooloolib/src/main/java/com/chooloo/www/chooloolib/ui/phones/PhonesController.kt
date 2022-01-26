package com.chooloo.www.chooloolib.ui.phones

import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.di.livedatafactory.LiveDataFactory
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.list.ListController
import javax.inject.Inject

class PhonesController<V : PhonesContract.View> @Inject constructor(
    view: V,
    private val phonesAdapter: PhonesAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val liveDataFactory: LiveDataFactory,
    private val clipboardManager: ClipboardManager,
    private val navigationsInteractor: NavigationsInteractor,
    private val permissionsInteractor: PermissionsInteractor
) :
    ListController<PhoneAccount, V>(view, phonesAdapter),
    PhonesContract.Controller<V> {

    private val phonesLiveData by lazy {
        liveDataFactory.allocPhonesProviderLiveData(if (view.contactId == 0L) null else view.contactId)
    }


    override val noResultsIconRes = R.drawable.ic_call_black_24dp
    override val noResultsTextRes = R.string.error_no_results_phones
    override val noPermissionsTextRes = R.string.error_no_permissions_phones


    override fun applyFilter(filter: String) {
        super.applyFilter(filter)
        phonesLiveData.filter = filter
    }

    override fun onItemClick(item: PhoneAccount) {
        navigationsInteractor.call(item.number)
    }

    override fun onItemLongClick(item: PhoneAccount) {
        clipboardManager.setPrimaryClip(
            ClipData.newPlainText("Copied number", item.number)
        )
        view.showMessage(R.string.number_copied_to_clipboard)
    }

    override fun fetchData(callback: (items: List<PhoneAccount>, hasPermissions: Boolean) -> Unit) {
        permissionsInteractor.runWithReadContactsPermissions {
            if (it) {
                phonesLiveData.observe(lifecycleOwner) { data ->
                    callback.invoke(data, true)
                }
            } else {
                callback.invoke(emptyList(), false)
            }
        }
    }
}
