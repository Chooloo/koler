package com.chooloo.www.koler.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.databinding.FragmentRecentBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.util.animateViews
import com.chooloo.www.koler.util.call.call
import com.chooloo.www.koler.util.deleteRecent
import com.chooloo.www.koler.util.getRecentById
import com.chooloo.www.koler.util.smsNumber

class RecentFragment : BaseFragment(), RecentMvpView {

    private val _presenter: RecentMvpPresenter<RecentMvpView> by lazy { RecentPresenter() }
    private val _recent by lazy { _activity.getRecentById(argsSafely.getLong(ARG_RECENT_ID)) }
    private val _binding by lazy { FragmentRecentBinding.inflate(layoutInflater) }

    companion object {
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
    ): View {
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter.attach(this)

        _binding.apply {
            recentTextName.text = _recent.cachedName
            recentTextDate.text = _recent.relativeTime
            recentTypeImage.apply {
                visibility = VISIBLE
                val drawable = ContextCompat.getDrawable(_activity, getCallTypeImage(_recent.type))
                setImageDrawable(drawable)
            }

            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
        }

        animateLayout()
    }

    override fun callRecent() {
        _recent.number.let { _activity.call(it) }
    }

    override fun smsRecent() {
        _activity.smsNumber(_recent.number)
    }

    override fun deleteRecent() {
        _activity.deleteRecent(_recent.id)
    }

    override fun animateLayout() {
        _binding.apply {
            animateViews(views = arrayOf(recentTextName, recentTextDate), isShow = true)
        }
    }
}