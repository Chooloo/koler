package com.chooloo.www.chooloolib.ui.callactions

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.adapter.CallActionsAdapter
import com.chooloo.www.chooloolib.databinding.CallActionsBinding
import com.chooloo.www.chooloolib.util.GridSpacingItemDecoration


class CallActions : ConstraintLayout {
    private val _binding: CallActionsBinding
    private var _isBluetoothActivated: Boolean = false
    private var _callActionsListener: CallActionsListener? = null
    private val _adapter by lazy { CallActionsAdapter() }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        _callActionsListener = getEmptyListener()

        _binding = CallActionsBinding.inflate(LayoutInflater.from(context), this, true)
        _binding.callActionsRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = _adapter
            val gridPadding = resources.getDimension(R.dimen.default_spacing_medium).toInt()
            addItemDecoration(GridSpacingItemDecoration(3, gridPadding, false))
        }

        _adapter.setOnCallActionClickListener {
            when (it.idRes) {
                R.string.call_action_id_hold -> _callActionsListener?.onHoldClick()
                R.string.call_action_id_mute -> _callActionsListener?.onMuteClick()
                R.string.call_action_id_swap -> _callActionsListener?.onSwapClick()
                R.string.call_action_id_add -> _callActionsListener?.onAddCallClick()
                R.string.call_action_id_merge -> _callActionsListener?.onMergeClick()
                R.string.call_action_id_keypad -> _callActionsListener?.onKeypadClick()
                R.string.call_action_id_speaker -> _callActionsListener?.onSpeakerClick()
            }
        }
    }


    var isHoldActivated: Boolean
        get() = _adapter.isCallActionActivated(R.string.call_action_id_hold)
        set(value) {
            _adapter.setCallActionActivated(R.string.call_action_id_hold, value)
        }

    var isMuteActivated: Boolean
        get() = _adapter.isCallActionActivated(R.string.call_action_id_mute)
        set(value) {
            _adapter.setCallActionActivated(R.string.call_action_id_mute, value)
        }

    var isSpeakerActivated: Boolean
        get() = _adapter.isCallActionActivated(R.string.call_action_id_speaker)
        set(value) {
            _adapter.setCallActionActivated(R.string.call_action_id_speaker, value)
        }

    var isBluetoothActivated: Boolean
        get() = _isBluetoothActivated
        set(value) {
            _isBluetoothActivated = value
            _adapter.setCallActionIcon(
                R.string.call_action_id_speaker,
                if (value) R.drawable.bluetooth_searching else R.drawable.volume_down
            )
            if (!value) {
                isSpeakerActivated = isSpeakerActivated
            }
        }


    var isHoldEnabled: Boolean
        get() = _adapter.isCallActionEnabled(R.string.call_action_id_hold)
        set(value) {
            _adapter.setCallActionEnabled(R.string.call_action_id_hold, value)
        }

    var isMuteEnabled: Boolean
        get() = _adapter.isCallActionEnabled(R.string.call_action_id_mute)
        set(value) {
            _adapter.setCallActionEnabled(R.string.call_action_id_mute, value)
        }

    var isSwapEnabled: Boolean
        get() = _adapter.isCallActionEnabled(R.string.call_action_id_swap)
        set(value) {
            _adapter.setCallActionEnabled(R.string.call_action_id_swap, value)
        }

    var isMergeEnabled: Boolean
        get() = _adapter.isCallActionEnabled(R.string.call_action_id_merge)
        set(value) {
            _adapter.setCallActionEnabled(R.string.call_action_id_merge, value)
        }

    var isSpeakerEnabled: Boolean
        get() = _adapter.isCallActionEnabled(R.string.call_action_id_speaker)
        set(value) {
            _adapter.setCallActionEnabled(R.string.call_action_id_speaker, value)
        }


    fun showSingleCallUI() {
        _adapter.addCallActions(
            listOf(
                callActionAdd,
                callActionMute,
                callActionHold,
                callActionKeypad,
                callActionSpeaker,
            )
        )
        _adapter.removeCallActions(
            listOf(
                callActionSwap,
                callActionMerge
            )
        )
    }

    fun showMultiCallUI() {
        _adapter.addCallActions(
            listOf(
                callActionAdd,
                callActionMute,
                callActionHold,
                callActionSwap,
                callActionKeypad,
                callActionMerge,
                callActionSpeaker,
            )
        )
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

    private val callActionKeypad by lazy {
        object : CallAction() {
            override val iconRes = R.drawable.dialpad
            override val idRes = R.string.call_action_id_keypad
        }
    }

    private val callActionAdd by lazy {
        object : CallAction() {
            override val iconRes = R.drawable.add
            override val idRes = R.string.call_action_id_add
        }
    }

    private val callActionMute by lazy {
        object : CallAction() {
            override val iconRes = R.drawable.mic
            override val checkedIconRes = R.drawable.mic_off
            override val idRes = R.string.call_action_id_mute
        }
    }
    private val callActionSpeaker by lazy {
        object : CallAction() {
            override val iconRes = R.drawable.volume_down
            override val checkedIconRes = R.drawable.volume_up
            override val idRes = R.string.call_action_id_speaker
        }
    }

    private val callActionHold by lazy {
        object : CallAction() {
            override val iconRes = R.drawable.pause
            override val idRes = R.string.call_action_id_hold
            override val checkedIconRes = R.drawable.play_arrow
        }
    }

    private val callActionMerge by lazy {
        object : CallAction() {
            override val iconRes = R.drawable.call_merge
            override val idRes = R.string.call_action_id_merge
        }
    }

    private val callActionSwap by lazy {
        object : CallAction() {
            override val iconRes = R.drawable.swap_calls
            override val idRes = R.string.call_action_id_swap
        }
    }
}