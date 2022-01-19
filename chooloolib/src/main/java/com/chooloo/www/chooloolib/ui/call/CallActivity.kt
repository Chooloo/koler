package com.chooloo.www.chooloolib.ui.call

import android.annotation.SuppressLint
import android.net.Uri
import android.text.format.DateUtils
import android.view.View
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.CallBinding
import com.chooloo.www.chooloolib.ui.base.BaseActivity

@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity(), CallContract.View {
    private lateinit var _controller: CallController<CallActivity>
    private val binding by lazy { CallBinding.inflate(layoutInflater) }

    override val contentView by lazy { binding.root }

    override var imageURI: Uri?
        get() = null
        set(value) {
            binding.callImage.setImageURI(value)
            binding.callImage.isVisible = value != null
        }

    override var nameText: String?
        get() = binding.callNameText.text.toString()
        set(value) {
            binding.callNameText.text = value
        }

    override var stateText: String?
        get() = binding.callStateText.text.toString()
        set(value) {
            val old = binding.callStateText.text.toString()
            binding.callStateText.text = value
            if (old != value) {
                component.animations.focus(binding.callStateText)
            }
        }

    override var stateTextColor: Int
        get() = binding.callStateText.currentTextColor
        set(value) {
            binding.callStateText.setTextColor(value)
        }

    override var isHoldEnabled: Boolean
        get() = binding.callActions.isHoldEnabled
        set(value) {
            binding.callActions.isHoldEnabled = value
        }

    override var isMuteEnabled: Boolean
        get() = binding.callActions.isMuteEnabled
        set(value) {
            binding.callActions.isMuteEnabled = value
        }

    override var isSwapEnabled: Boolean
        get() = binding.callActions.isSwapEnabled
        set(value) {
            binding.callActions.isSwapEnabled = value
        }

    override var isMergeEnabled: Boolean
        get() = binding.callActions.isMergeEnabled
        set(value) {
            binding.callActions.isMergeEnabled = value
        }

    override var isManageEnabled: Boolean
        get() = binding.callManageButton.isVisible
        set(value) {
            binding.callManageButton.isVisible = value
        }

    override var isSpeakerEnabled: Boolean
        get() = binding.callActions.isSpeakerEnabled
        set(value) {
            binding.callActions.isSpeakerEnabled = value
        }

    override var isMuteActivated: Boolean
        get() = binding.callActions.isMuteActivated
        set(value) {
            binding.callActions.isMuteActivated = value
        }

    override var isHoldActivated: Boolean
        get() = binding.callActions.isHoldActivated
        set(value) {
            binding.callActions.isHoldActivated = value
        }

    override var isSpeakerActivated: Boolean
        get() = binding.callActions.isSpeakerActivated
        set(value) {
            binding.callActions.isSpeakerActivated = value
        }

    override var isBluetoothActivated: Boolean
        get() = binding.callActions.isBluetoothActivated
        set(value) {
            binding.callActions.isBluetoothActivated = value
        }


    override fun onSetup() {
        _controller = CallController(this)

        binding.apply {
            imageURI = null
            callActions.setCallActionsListener(_controller)
            callAnswerButton.setOnClickListener { _controller.onAnswerClick() }
            callRejectButton.setOnClickListener { _controller.onRejectClick() }
            callManageButton.setOnClickListener { _controller.onManageClick() }
        }
    }

    override fun showActiveCallUI() {
        showActiveLayout()
        binding.callActions.showSingleCallUI()
    }

    override fun showIncomingCallUI() {
        transitionLayoutTo(R.id.constraint_set_incoming_call)
    }

    override fun showMultiActiveCallUI() {
        showActiveLayout()
        binding.callActions.showMultiCallUI()
    }

    override fun setElapsedTime(duration: Long?) {
        duration?.let {
            component.animations.show(binding.callTimeText, true)
            binding.callTimeText.text = DateUtils.formatElapsedTime(duration / 1000)
        } ?: run {
            component.animations.hide(
                binding.callTimeText,
                ifVisible = true, goneOrInvisible = false
            )
        }
    }

    override fun showHoldingBanner(number: String) {
        binding.callBanner.text = number
        if (binding.callBanner.visibility != View.VISIBLE) {
            binding.callBanner.visibility = View.VISIBLE
            component.animations.show(binding.callBanner, true)
            component.animations.focus(binding.callBanner)
        }
    }

    override fun hideHoldingBanner() {
        component.animations.hide(binding.callBanner, ifVisible = true, goneOrInvisible = false)
    }


    private fun showActiveLayout() {
        transitionLayoutTo(R.id.constraint_set_active_call)
        if (binding.callActions.visibility != View.VISIBLE) {
            component.animations.show(binding.callActions, true)
        }
    }

    private fun transitionLayoutTo(constraintRes: Int) {
        if (binding.root.currentState != constraintRes) {
            binding.root.setTransition(binding.root.currentState, constraintRes)
            binding.root.transitionToEnd()
        }
    }
}
