package com.chooloo.www.koler.ui.phones

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.koler.ui.list.ListFragment

class PhonesFragment :
    ListFragment<PhoneAccount, PhonesAdapter>(),
    PhonesContract.View {

    override val contactId by lazy { args.getLong(ARG_CONTACT_ID) }
    override lateinit var presenter: PhonesPresenter<PhonesFragment>
    override lateinit var bottomFragment: BottomFragment<Fragment>

    override fun onSetup() {
        presenter = PhonesPresenter(this)
        super.onSetup()
    }

    override fun showItem(item: PhoneAccount) {
        return
    }

    override fun hideItem() {
        return
    }


    companion object {
        fun newInstance(
            contactId: Long? = null,
            isSearchable: Boolean,
            isHideNoResults: Boolean = false
        ) =
            PhonesFragment().apply {
                arguments = Bundle().apply {
                    contactId?.let { putLong(ARG_CONTACT_ID, it) }
                    putBoolean(ARG_IS_SEARCHABLE, isSearchable)
                    putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
                }
            }
    }
}