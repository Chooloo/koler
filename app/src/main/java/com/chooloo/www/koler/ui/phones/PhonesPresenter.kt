package com.chooloo.www.koler.ui.phones

import android.content.ClipData
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.contentresolver.PhonesContentResolver
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.ui.list.ListPresenter

class PhonesPresenter<V : PhonesContract.View>(view: V) :
    ListPresenter<PhoneAccount, V>(view),
    PhonesContract.Presenter<V> {

    override val adapter by lazy { PhonesAdapter(boundComponent) }

    private val phonesLiveData by lazy {
        val contactId = if (view.contactId == 0L) null else view.contactId
        boundComponent.liveDataFactory.allocPhonesProviderLiveData(contactId)
    }


    override val requiredPermissions
        get() = PhonesContentResolver.REQUIRED_PERMISSIONS

    override val noResultsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_results_phones)

    override val noPermissionsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_permissions_phones)


    override fun observeData() {
        phonesLiveData.observe(boundComponent.lifecycleOwner, this::onDataChanged)
    }

    override fun applyFilter(filter: String) {
        phonesLiveData.filter = filter
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

    override fun convertDataToListBundle(data: ArrayList<PhoneAccount>) =
        ListBundle.fromPhones(data, boundComponent.stringInteractor, true)
}
