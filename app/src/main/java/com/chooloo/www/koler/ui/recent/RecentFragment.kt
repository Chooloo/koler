package com.chooloo.www.koler.ui.recent

import ContactsUtils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.databinding.RecentBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.recentpreferences.RecentPreferencesFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment
import com.chooloo.www.koler.call.CallManager
import com.chooloo.www.koler.util.getElapsedTimeString
import java.util.*

class RecentFragment : BaseFragment(), RecentContract.View {
    private val _recentsManager by lazy { RecentsManager(baseActivity) }
    private val _contactsManager by lazy { ContactsUtils(baseActivity) }
    private val _binding by lazy { RecentBinding.inflate(layoutInflater) }
    private val _contact by lazy { _contactsManager.lookupAccountByNumber(_recent.number) }
    private val _presenter by lazy { RecentPresenter<RecentContract.View>(this) }
    private val _isBlocked by lazy { _contactsManager.queryIsNumberBlocked(_recent.number) }
    private val _recent by lazy { _recentsManager.getRecentById(argsSafely.getLong(ARG_RECENT_ID)) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _binding.apply {
            recentTextCaption.apply {
                visibility = VISIBLE
                text = _recent.relativeTime
                if (_recent.duration > 0) {
                    text = "$text, ${getElapsedTimeString(_recent.duration)}"
                }
                if (_isBlocked) {
                    text = "$text, ${getString(R.string.error_blocked).toUpperCase(Locale.ROOT)})"
                }
            }

            recentTypeImage.apply {
                visibility = VISIBLE
                setImageDrawable(getDrawable(baseActivity, getCallTypeImage(_recent.type)))
            }
            recentTextName.text = _recent.cachedName ?: _recent.number

            recentButtonContact.visibility = if (_contact != null) VISIBLE else GONE
            recentButtonAddContact.visibility = if (_contact != null) GONE else VISIBLE

            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonMenu.setOnClickListener { _presenter.onActionMenu() }
            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
            recentButtonContact.setOnClickListener { _presenter.onActionOpenContact() }
            recentButtonAddContact.setOnClickListener { _presenter.onActionAddContact() }
            recentButtonShowHistory.setOnClickListener { _presenter.onActionShowHistory() }
        }
    }


    //region recents view

    override fun showMenu() {
        BottomFragment(RecentPreferencesFragment.newInstance(_recent.number)).show(
            childFragmentManager,
            null
        )
    }

    override fun smsRecent() {
        _contactsManager.openSmsView(_recent.number)
    }

    override fun addContact() {
        _contactsManager.openAddContactView(_recent.number)
    }

    override fun callRecent() {
        _recent.number.let { CallManager.call(baseActivity, it) }
    }

    override fun openContact() {
        _contact?.contactId?.let {
            BottomFragment(ContactFragment.newInstance(it)).show(
                baseActivity.supportFragmentManager,
                ContactFragment.TAG
            )
        }
    }

    override fun openHistory() {
        BottomFragment(
            RecentsFragment.newInstance(isSearchable = false, filter = _recent.number)
        ).show(baseActivity.supportFragmentManager, ContactFragment.TAG)
    }

    override fun deleteRecent() {
        _recentsManager.deleteRecent(_recent.id)
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