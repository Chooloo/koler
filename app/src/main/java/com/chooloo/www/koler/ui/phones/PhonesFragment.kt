package com.chooloo.www.koler.ui.phones

import PhoneAccount
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.livedata.PhoneProviderLiveData
import com.chooloo.www.koler.ui.contact.ContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.call.CallManager

class PhonesFragment : ListFragment<PhoneAccount, PhonesAdapter>(), PhonesContract.View {
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _presenter by lazy { PhonesPresenter<PhonesContract.View>(this) }
    private val _phonesLiveData by lazy { PhoneProviderLiveData(baseActivity, _contactId) }

    override val adapter by lazy { PhonesAdapter(baseActivity) }
    override val requiredPermissions = PhoneContentResolver.REQUIRED_PERMISSIONS
    override val noResultsMessage by lazy { getString(R.string.error_no_results_phones) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions_phones) }


    //region list fragment

    override fun onAttachData() {
        _phonesLiveData.observe(viewLifecycleOwner, _presenter::onPhonesChanged)
    }

    override fun onItemClick(item: PhoneAccount) {
        _presenter.onPhoneItemClick(item)
    }

    override fun onItemLongClick(item: PhoneAccount) {
        _presenter.onPhoneLongItemClick(item)
    }

    //endregion

    //region phones view

    override fun callNumber(number: String) {
        CallManager.call(baseActivity, number)
    }

    override fun clipboardText(text: String) {
        componentRoot.clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied number", text))
    }

    override fun convertBundleToList(phones: ArrayList<PhoneAccount>): ListBundle<PhoneAccount> {
        return ListBundle.fromPhones(baseActivity, phones, true)
    }

    //endregion


    companion object {
        fun newInstance(contactId: Long, isSearchable: Boolean, isHideNoResults: Boolean = false) =
            PhonesFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_COMPACT, true)
                    putLong(ARG_CONTACT_ID, contactId)
                    putBoolean(ARG_IS_SEARCHABLE, isSearchable)
                    putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
                }
            }
    }
}