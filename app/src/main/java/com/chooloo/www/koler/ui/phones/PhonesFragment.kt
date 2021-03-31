package com.chooloo.www.koler.ui.phones

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.data.PhonesBundle
import com.chooloo.www.koler.livedata.PhoneProviderLiveData
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.call.call

class PhonesFragment : ListFragment<PhonesAdapter>(), PhonesContract.View {
    private val _presenter by lazy { PhonesPresenter<PhonesContract.View>() }
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _phonesLiveData by lazy { PhoneProviderLiveData(_activity, _contactId) }

    //region list args
    override val requiredPermissions get() = _phonesLiveData.requiredPermissions
    override val noResultsMessage by lazy { getString(R.string.error_no_results_phones) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions_phones) }
    override val adapter by lazy {
        PhonesAdapter().apply {
            setOnItemClickListener(_presenter::onPhoneItemClick)
        }
    }
    //endregion

    companion object {
        private const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = PhonesFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }

    override fun onSetup() {
        super.onSetup()
        _presenter.attach(this)
    }

    override fun onAttachData() {
        _phonesLiveData.observe(viewLifecycleOwner, _presenter::onPhonesChanged)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun updatePhoneAccounts(phonesBundle: PhonesBundle) {
        adapter.data = phonesBundle.getListBundleByType(_activity)
    }

    override fun callNumber(number: String) {
        _activity.call(number)
    }
}