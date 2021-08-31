package com.chooloo.www.koler.ui.phones

import android.content.ClipData
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListPresenter

class PhonesPresenter<V : ListContract.View<PhoneAccount>>(view: V) :
    ListPresenter<PhoneAccount, V>(view),
    ListContract.Presenter<PhoneAccount, V> {

    override val requiredPermissions
        get() = PhoneContentResolver.REQUIRED_PERMISSIONS

    override val noResultsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_results_phones)

    override val noPermissionsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_permissions_phones)


    override fun observeData() {
        boundComponent.phonesProviderLiveData.observe(
            boundComponent.lifecycleOwner,
            this::onDataChanged
        )
    }

    override fun onItemClick(item: PhoneAccount) {
        boundComponent.navigationInteractor.call(item.number)
    }

    override fun onItemLongClick(item: PhoneAccount) {
        boundComponent.clipboardManager.setPrimaryClip(
            ClipData.newPlainText("Copied number", item.number)
        )
        view.showMessage(R.string.number_copied_to_clipboard)
    }

    override fun applyFilter(filter: String) {
        boundComponent.phonesProviderLiveData.filter = filter
    }
}
