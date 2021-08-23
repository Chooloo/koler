package com.chooloo.www.koler.ui.phones

import PhoneAccount
import android.os.Bundle
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.contact.ContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListFragment

class PhonesFragment :
    ListFragment<PhoneAccount, PhonesAdapter>(),
    ListContract.View<PhoneAccount> {

    override val adapter by lazy { PhonesAdapter(baseActivity) }
    override val presenter: PhonesPresenter<PhonesFragment> by lazy { PhonesPresenter(this) }

    override fun showItem(item: PhoneAccount) {
        return
    }

    override fun updateData(dataList: ArrayList<PhoneAccount>) {
        adapter.data = ListBundle.fromPhones(baseActivity, dataList, true)
    }


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