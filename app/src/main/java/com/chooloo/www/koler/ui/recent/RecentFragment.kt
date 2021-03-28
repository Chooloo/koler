package com.chooloo.www.koler.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.databinding.FragmentRecentBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.util.addContact
import com.chooloo.www.koler.util.call.call
import com.chooloo.www.koler.util.deleteRecent
import com.chooloo.www.koler.util.getRecentById
import com.chooloo.www.koler.util.smsNumber

class RecentFragment : BaseFragment(), RecentContract.View {
    private val _presenter by lazy { RecentPresenter<RecentContract.View>() }
    private val _binding by lazy { FragmentRecentBinding.inflate(layoutInflater) }
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
            recentTextName.text = _recent.cachedName ?: _recent.number
            recentButtonAddContact.visibility = if (_recent.cachedName == null) VISIBLE else GONE

            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
            recentButtonAddContact.setOnClickListener { _presenter.onActionAddContact() }
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

    override fun deleteRecent() {
        _activity.deleteRecent(_recent.id)
    }
}