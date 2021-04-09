package com.chooloo.www.koler.ui.phones

import PhoneAccount
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.data.PhonesBundle
import com.chooloo.www.koler.livedata.PhoneProviderLiveData
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.call.call

class PhonesFragment : ListFragment<PhoneAccount, PhonesAdapter>(), PhonesContract.View {
    private val _presenter by lazy { PhonesPresenter<PhonesContract.View>() }
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _phonesLiveData by lazy { PhoneProviderLiveData(_activity, _contactId) }
    private val _clipboardManager by lazy { _activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    //region list args
    override val requiredPermissions get() = _phonesLiveData.requiredPermissions
    override val noResultsMessage by lazy { getString(R.string.error_no_results_phones) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions_phones) }
    override val adapter by lazy {
        PhonesAdapter().apply {
            setOnItemClickListener(_presenter::onPhoneItemClick)
            setOnItemLongClickListener(_presenter::onPhoneLongItemClick)
            isCompact = true
        }
    }
    //endregion

    companion object {
        private const val ARG_CONTACT_ID = "contact_id"

        fun newInstance(contactId: Long, isCompact: Boolean = false) = PhonesFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CONTACT_ID, contactId)
                putBoolean(ARG_IS_COMPACT, isCompact)
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
        adapter.data = phonesBundle.getListBundleByType(_activity, true)
    }

    override fun callNumber(number: String) {
        _activity.call(number)
    }

    override fun clipboardText(text: String) {
        _clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied number", text))
    }
}