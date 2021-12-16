package com.chooloo.www.koler.ui.recent

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.chooloo.www.koler.databinding.RecentBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.recentpreferences.RecentPreferencesFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment

class RecentFragment : BaseFragment(), RecentContract.View {
    private lateinit var _presenter: RecentController<RecentFragment>
    private val _binding by lazy { RecentBinding.inflate(layoutInflater) }

    override val recentId by lazy { args.getLong(ARG_RECENT_ID) }

    override var recentName: String?
        get() = _binding.recentTextName.text.toString()
        set(value) {
            _binding.recentTextName.text = value
        }

    override var recentCaption: String?
        get() = _binding.recentTextCaption.text.toString()
        set(value) {
            _binding.recentTextCaption.text = value
            _binding.recentTextCaption.visibility = if (value != null) VISIBLE else GONE
        }

    override var recentImage: Drawable?
        get() = _binding.recentTypeImage.drawable
        set(value) {
            _binding.recentTypeImage.setImageDrawable(value)
        }

    override var isContactVisible: Boolean
        get() = _binding.recentButtonContact.visibility == VISIBLE
        set(value) {
            _binding.recentButtonContact.visibility = if (value) VISIBLE else GONE
        }

    override var isAddContactVisible: Boolean
        get() = _binding.recentButtonAddContact.visibility == VISIBLE
        set(value) {
            _binding.recentButtonAddContact.visibility = if (value) VISIBLE else GONE
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _presenter = RecentController(this)
        _binding.apply {
            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonMenu.setOnClickListener { _presenter.onActionMenu() }
            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
            recentButtonContact.setOnClickListener { _presenter.onActionOpenContact() }
            recentButtonAddContact.setOnClickListener { _presenter.onActionAddContact() }
            recentButtonShowHistory.setOnClickListener { _presenter.onActionShowHistory() }
        }
    }


    override fun showRecentMenu(number: String) {
        BottomFragment(RecentPreferencesFragment.newInstance(number)).show(
            childFragmentManager,
            null
        )
    }

    override fun openHistoryView(number: String) {
        BottomFragment(RecentsFragment.newInstance(number)).show(
            baseActivity.supportFragmentManager,
            ContactFragment.TAG
        )
    }

    override fun openContactView(contactId: Long) {
        BottomFragment(ContactFragment.newInstance(contactId)).show(
            baseActivity.supportFragmentManager,
            ContactFragment.TAG
        )
    }


    //endregion


    companion object {
        const val TAG = "recent_fragment"
        const val ARG_RECENT_ID = "recent_id"

        fun newInstance(recentId: Long) = RecentFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_RECENT_ID, recentId)
            }
        }
    }
}