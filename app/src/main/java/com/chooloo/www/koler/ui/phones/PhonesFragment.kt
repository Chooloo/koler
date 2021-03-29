package com.chooloo.www.koler.ui.phones

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.data.PhonesBundle
import com.chooloo.www.koler.livedata.PhonesProviderLiveData
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.permissions.runWithPermissions

class PhonesFragment : ListFragment<PhonesAdapter>(), PhonesContract.View {
    private val _presenter by lazy { PhonesPresenter<PhonesContract.View>() }
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _phonesLiveData by lazy { PhonesProviderLiveData(_activity, _contactId) }

    companion object {
        private const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long) = PhonesFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
            }
        }
    }

    override fun onGetAdapter() = PhonesAdapter()

    override fun onSetup() {
        super.onSetup()
        _presenter.attach(this)
        runWithPermissions(
            _phonesLiveData.requiredPermissions,
            {
                _phonesLiveData.observe(viewLifecycleOwner, _presenter::onPhonesChanged)
                emptyMessage = getString(R.string.error_no_results)
            },
            blockedCallback = _presenter::onNoPermissions
        )
        _presenter.onPhonesChanged(_phonesLiveData.onGetContentResolver().content)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun updatePhoneAccounts(phonesBundle: PhonesBundle) {
        listAdapter.data = phonesBundle.getListBundleByType(_activity)
    }
}