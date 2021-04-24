package com.chooloo.www.koler.ui.recent

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
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.call
import com.chooloo.www.koler.util.permissions.runWithPrompt

class RecentFragment : BaseFragment(), RecentContract.View {
    private val _presenter by lazy { RecentPresenter<RecentContract.View>() }
    private val _isBlocked by lazy { _activity.isNumberBlocked(_recent.number) }
    private val _contact by lazy { _activity.lookupContactNumber(_recent.number) }
    private val _binding by lazy { RecentBinding.inflate(layoutInflater) }
    private val _recent by lazy { _activity.getRecentById(argsSafely.getLong(ARG_RECENT_ID)) }

    companion object {
        const val TAG = "recent_fragment"
        const val ARG_RECENT_ID = "recent_id"

        fun newInstance(recentId: Long) = RecentFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_RECENT_ID, recentId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter.attach(this)

        _binding.apply {
            recentTextDate.apply {
                visibility = VISIBLE
                text = _recent.relativeTime
            }
            recentTypeImage.apply {
                visibility = VISIBLE
                setImageDrawable(getDrawable(_activity, getCallTypeImage(_recent.type)))
            }
            recentTextDuration.apply {
                text = getElapsedTimeString(_recent.duration)
                visibility = if (_recent.duration == 0.toLong()) GONE else VISIBLE
            }
            recentTextName.text = _recent.cachedName ?: _recent.number

            recentTextBlocked.visibility = if (_isBlocked) VISIBLE else GONE
            recentButtonContact.visibility = if (_contact != null) VISIBLE else GONE
            recentButtonAddContact.visibility = if (_contact != null) GONE else VISIBLE
            recentTextSeperator.visibility = if (_recent.duration == 0.toLong()) GONE else VISIBLE

            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonMenu.setOnClickListener { _presenter.onActionMenu() }
            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
            recentButtonContact.setOnClickListener { _presenter.onActionOpenContact() }
            recentButtonAddContact.setOnClickListener { _presenter.onActionAddContact() }
            recentButtonShowHistory.setOnClickListener { _presenter.onActionShowHistory() }
        }
    }

    override fun showMenu() {
        BottomFragment(RecentPreferencesFragment.newInstance(_recent.number)).show(
            childFragmentManager,
            null
        )
    }

    override fun smsRecent() {
        _activity.smsNumber(_recent.number)
    }

    override fun addContact() {
        _activity.addContact(_recent.number)
    }

    override fun callRecent() {
        _recent.number.let { _activity.call(it) }
    }

    override fun openContact() {
        _contact?.contactId?.let {
            BottomFragment(ContactFragment.newInstance(it)).show(
                _activity.supportFragmentManager,
                ContactFragment.TAG
            )
        }
    }

    override fun openHistory() {
        BottomFragment(
            RecentsFragment.newInstance(false, false, _recent.number)
        ).show(_activity.supportFragmentManager, ContactFragment.TAG)
    }

    override fun deleteRecent() {
        _activity.apply {
            runWithPrompt(R.string.warning_delete_recent) {
                deleteRecent(_recent.id)
            }
        }
    }
}