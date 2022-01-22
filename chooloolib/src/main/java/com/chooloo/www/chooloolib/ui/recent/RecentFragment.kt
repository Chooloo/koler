package com.chooloo.www.chooloolib.ui.recent

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.databinding.RecentBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment

class RecentFragment : BaseFragment(), RecentContract.View {
    private lateinit var controller: RecentController<RecentFragment>
    private val binding by lazy { RecentBinding.inflate(layoutInflater) }

    override val contentView by lazy { binding.root }
    override val recentId by lazy { args.getLong(ARG_RECENT_ID) }

    override var recentName: String?
        get() = binding.recentTextName.text.toString()
        set(value) {
            binding.recentTextName.text = value
        }

    override var recentCaption: String?
        get() = binding.recentTextCaption.text.toString()
        set(value) {
            binding.recentTextCaption.text = value
            binding.recentTextCaption.visibility = if (value != null) VISIBLE else GONE
        }

    override var recentImage: Drawable?
        get() = binding.recentTypeImage.drawable
        set(value) {
            binding.recentTypeImage.setImageDrawable(value)
        }

    override var isContactVisible: Boolean
        get() = binding.recentButtonContact.isVisible
        set(value) {
            binding.recentButtonContact.isVisible = value
        }

    override var isAddContactVisible: Boolean
        get() = binding.recentButtonAddContact.isVisible
        set(value) {
            binding.recentButtonAddContact.isVisible = value
        }

    override var isBlockButtonVisible: Boolean
        get() = binding.recentButtonBlock.isVisible
        set(value) {
            binding.recentButtonBlock.isVisible = value
        }

    override var isBlockButtonActivated: Boolean
        get() = binding.recentButtonBlock.isActivated
        set(value) {
            binding.recentButtonBlock.isActivated = value
        }


    override fun onSetup() {
        controller = RecentController(this)
        binding.apply {
            recentButtonSms.setOnClickListener { controller.onActionSms() }
            recentButtonCall.setOnClickListener { controller.onActionCall() }
            recentButtonDelete.setOnClickListener { controller.onActionDelete() }
            recentButtonContact.setOnClickListener { controller.onActionOpenContact() }
            recentButtonAddContact.setOnClickListener { controller.onActionAddContact() }
            recentButtonShowHistory.setOnClickListener { controller.onActionShowHistory() }
            recentButtonBlock.setOnClickListener { controller.onActionBlock(!isBlockButtonActivated) }
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