package com.chooloo.www.koler.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.databinding.FragmentRecentBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.call

class RecentFragment : BaseFragment(), RecentContract.View {
    private val _contact by lazy { _activity.lookupContact(_recent.number) }
    private val _presenter by lazy { RecentPresenter<RecentContract.View>() }
    private val _binding by lazy { FragmentRecentBinding.inflate(layoutInflater) }
    private val _recent by lazy { _activity.getRecentById(argsSafely.getLong(ARG_RECENT_ID)) }
    private val _isBlocked by lazy { _activity.isNumberBlocked(_recent.number) }

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
            recentTextName.text = _recent.cachedName ?: _recent.number
            recentTextDuration.text = getElapsedTimeString(_recent.duration)

            recentTextBlocked.visibility = if (_isBlocked) VISIBLE else GONE
            recentButtonBlock.visibility = if (_isBlocked) GONE else VISIBLE
            recentButtonUnblock.visibility = if (_isBlocked) VISIBLE else GONE
            recentButtonContact.visibility = if (_contact != null) VISIBLE else GONE
            recentButtonAddContact.visibility = if (_contact != null) GONE else VISIBLE

            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
            recentButtonBlock.setOnClickListener { _presenter.onActionBlockNumber() }
            recentButtonContact.setOnClickListener { _presenter.onActionOpenContact() }
            recentButtonUnblock.setOnClickListener { _presenter.onActionUnblockNumber() }
            recentButtonAddContact.setOnClickListener { _presenter.onActionAddContact() }
            recentButtonShowHistory.setOnClickListener { _presenter.onActionShowHistory() }
        }
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

    override fun blockNumber() {
        _activity.blockNumber(_recent.number)
    }

    override fun deleteRecent() {
        AlertDialog.Builder(_activity)
            .setCancelable(true)
            .setTitle(getString(R.string.warning_delete_recent))
            .setPositiveButton(getString(R.string.action_yes)) { _, _ ->
                _activity.deleteRecent(_recent.id)
                finish()
            }
            .setNegativeButton(getString(R.string.action_no)) { _, _ -> }
            .create()
            .show()
    }

    override fun unblockNumber() {
        _activity.unblockNumber(_recent.number)
    }
}