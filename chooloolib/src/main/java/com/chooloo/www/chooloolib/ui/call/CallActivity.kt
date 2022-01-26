package com.chooloo.www.chooloolib.ui.call

import android.annotation.SuppressLint
import android.net.Uri
import android.text.format.DateUtils
import android.view.View
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.CallBinding
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity(), CallContract.View {
    override val contentView by lazy { binding.root }

    private val binding by lazy { CallBinding.inflate(layoutInflater) }

    @Inject lateinit var animationsInteractor: AnimationsInteractor
    @Inject lateinit var controller: CallContract.Controller<CallActivity>

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
                animationsInteractor.focus(binding.callStateText)
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
        binding.apply {
            imageURI = null
            callActions.setCallActionsListener(controller)
            callAnswerButton.setOnClickListener { controller.onAnswerClick() }
            callRejectButton.setOnClickListener { controller.onRejectClick() }
            callManageButton.setOnClickListener { controller.onManageClick() }
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
            animationsInteractor.show(binding.callTimeText, true)
            binding.callTimeText.text = DateUtils.formatElapsedTime(duration / 1000)
        } ?: run {
            animationsInteractor.hide(
                binding.callTimeText,
                ifVisible = true, goneOrInvisible = false
            )
        }
    }

    override fun showHoldingBanner(number: String) {
        binding.callBanner.text = number
        if (binding.callBanner.visibility != View.VISIBLE) {
            binding.callBanner.visibility = View.VISIBLE
            animationsInteractor.show(binding.callBanner, true)
            animationsInteractor.focus(binding.callBanner)
        }
    }

    override fun hideHoldingBanner() {
        animationsInteractor.hide(binding.callBanner, ifVisible = true, goneOrInvisible = false)
    }


    private fun showActiveLayout() {
        transitionLayoutTo(R.id.constraint_set_active_call)
        if (binding.callActions.visibility != View.VISIBLE) {
            animationsInteractor.show(binding.callActions, true)
        }
    }

    private fun transitionLayoutTo(constraintRes: Int) {
        if (binding.root.currentState != constraintRes) {
            binding.root.setTransition(binding.root.currentState, constraintRes)
            binding.root.transitionToEnd()
        }
    }
}
