package com.chooloo.www.chooloolib.ui.recent

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.databinding.RecentBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.base.BottomFragment
import com.chooloo.www.chooloolib.ui.contact.ContactFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsFragment

class RecentFragment : BaseFragment(), RecentContract.View {
    private lateinit var _presenter: RecentController<RecentFragment>
    private val _binding by lazy { RecentBinding.inflate(layoutInflater) }

    override val contentView by lazy { _binding.root }
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
        get() = _binding.recentButtonContact.isVisible
        set(value) {
            _binding.recentButtonContact.isVisible = value
        }

    override var isAddContactVisible: Boolean
        get() = _binding.recentButtonAddContact.isVisible
        set(value) {
            _binding.recentButtonAddContact.isVisible = value
        }


    override fun onSetup() {
        _presenter = RecentController(this)
        _binding.apply {
            recentButtonSms.setOnClickListener { _presenter.onActionSms() }
            recentButtonCall.setOnClickListener { _presenter.onActionCall() }
            recentButtonDelete.setOnClickListener { _presenter.onActionDelete() }
            recentButtonContact.setOnClickListener { _presenter.onActionOpenContact() }
            recentButtonAddContact.setOnClickListener { _presenter.onActionAddContact() }
            recentButtonShowHistory.setOnClickListener { _presenter.onActionShowHistory() }
        }
    }


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