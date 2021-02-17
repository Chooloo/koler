package com.chooloo.www.koler.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.databinding.FragmentRecentBinding
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.util.animateViews
import com.chooloo.www.koler.util.call.call
import com.chooloo.www.koler.util.deleteRecent
import com.chooloo.www.koler.util.getRecentById
import com.chooloo.www.koler.util.smsNumber

class RecentFragment : BaseFragment(), RecentMvpView {

    private lateinit var _presenter: RecentPresenter<RecentMvpView>
    private lateinit var _recent: Recent
    private lateinit var _binding: FragmentRecentBinding

    companion object {
        const val ARG_RECENT_ID = "recent_id"

        fun newInstance(recentId: Long) = RecentFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_RECENT_ID, recentId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun onSetup() {
        _presenter = RecentPresenter()
        _presenter.attach(this)

        _recent = _activity.getRecentById(argsSafely.getLong(ARG_RECENT_ID))

        _binding.apply {
            recentTextName.text = _recent.cachedName
            recentTextDate.text = _recent.relativeTime
            recentTypeImage.apply {
                visibility = VISIBLE
                setImageDrawable(ContextCompat.getDrawable(_activity, getCallTypeImage(_recent.type)))
            }

            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
        }

        animateLayout()
    }

    override fun call() {
        _recent.number.let { _activity.call(it) }
    }

    override fun sms() {
        _activity.smsNumber(_recent.number)
    }

    override fun delete() {
        _activity.deleteRecent(_recent.id)
    }

    override fun animateLayout() {
        _binding.apply {
            animateViews(views = arrayOf(recentTextName, recentTextDate), isShow = true)
        }
    }
}