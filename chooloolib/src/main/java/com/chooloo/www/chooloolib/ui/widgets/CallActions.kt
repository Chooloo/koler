package com.chooloo.www.chooloolib.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.motion.widget.MotionLayout
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.CallActionsBinding

class CallActions : MotionLayout {
    private val _binding: CallActionsBinding
    private var _isBluetoothActivated: Boolean = false
    private var _callActionsListener: CallActionsListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        _callActionsListener = getEmptyListener()

        _binding = CallActionsBinding.inflate(LayoutInflater.from(context), this, true)
        _binding.callActionSwap.setOnClickListener { _callActionsListener?.onSwapClick() }
        _binding.callActionHold.setOnClickListener { _callActionsListener?.onHoldClick() }
        _binding.callActionMute.setOnClickListener { _callActionsListener?.onMuteClick() }
        _binding.callActionMerge.setOnClickListener { _callActionsListener?.onMergeClick() }
        _binding.callActionKeypad.setOnClickListener { _callActionsListener?.onKeypadClick() }
        _binding.callActionSpeaker.setOnClickListener { _callActionsListener?.onSpeakerClick() }
        _binding.callActionAddCall.setOnClickListener { _callActionsListener?.onAddCallClick() }
    }


    private fun transitionLayoutTo(constraintRes: Int) {
        if (_binding.root.currentState != constraintRes) {
            _binding.root.setTransition(_binding.root.currentState, constraintRes)
            _binding.root.transitionToEnd()
        }
    }


    var isHoldActivated: Boolean
        get() = _binding.callActionHold.isActivated
        set(value) {
            _binding.callActionHold.isActivated = value
        }

    var isMuteActivated: Boolean
        get() = _binding.callActionMute.isActivated
        set(value) {
            _binding.callActionMute.isActivated = value
        }

    var isSpeakerActivated: Boolean
        get() = _binding.callActionSpeaker.isActivated
        set(value) {
            _binding.callActionSpeaker.isActivated = value
        }

    var isBluetoothActivated: Boolean
        get() = _isBluetoothActivated
        set(value) {
            _isBluetoothActivated = value
            _binding.callActionSpeaker.setDefaultIcon(if (value) R.drawable.round_bluetooth_audio_24 else R.drawable.round_volume_down_24)
        }


    var isHoldEnabled: Boolean
        get() = _binding.callActionHold.isEnabled
        set(value) {
            _binding.callActionHold.isEnabled = value
        }

    var isMuteEnabled: Boolean
        get() = _binding.callActionMute.isEnabled
        set(value) {
            _binding.callActionMute.isEnabled = value
        }

    var isSwapEnabled: Boolean
        get() = _binding.callActionSwap.isEnabled
        set(value) {
            _binding.callActionSwap.isEnabled = value
        }

    var isMergeEnabled: Boolean
        get() = _binding.callActionMerge.isEnabled
        set(value) {
            _binding.callActionMerge.isEnabled = value
        }

    var isSpeakerEnabled: Boolean
        get() = _binding.callActionSpeaker.isEnabled
        set(value) {
            _binding.callActionSpeaker.isEnabled = value
        }


    fun showSingleCallUI() {
        transitionLayoutTo(R.id.constraint_set_single_call)
        _binding.callActionSwap.visibility = GONE
        _binding.callActionMerge.visibility = GONE
    }

    fun showMultiCallUI() {
        transitionLayoutTo(R.id.constraint_set_multi_call)
        _binding.callActionSwap.visibility = VISIBLE
        _binding.callActionMerge.visibility = VISIBLE
    }

    fun setCallActionsListener(callActionsListener: CallActionsListener?) {
        _callActionsListener = callActionsListener
    }


    private fun getEmptyListener() = object : CallActionsListener {
        override fun onHoldClick() {}
        override fun onMuteClick() {}
        override fun onSwapClick() {}
        override fun onMergeClick() {}
        override fun onKeypadClick() {}
        override fun onSpeakerClick() {}
        override fun onAddCallClick() {}
    }


    interface CallActionsListener {
        fun onHoldClick()
        fun onMuteClick()
        fun onSwapClick()
        fun onMergeClick()
        fun onKeypadClick()
        fun onSpeakerClick()
        fun onAddCallClick()
    }
}