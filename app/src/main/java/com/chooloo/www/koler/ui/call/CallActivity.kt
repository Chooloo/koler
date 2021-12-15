package com.chooloo.www.koler.ui.call

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.CallBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment

@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity(), CallContract.View {
    private lateinit var _presenter: CallController<CallActivity>
    private val _binding by lazy { CallBinding.inflate(layoutInflater) }

    override var imageURI: Uri?
        get() = null
        set(value) {
            _binding.callImage.setImageURI(value)
        }

    override var nameText: String?
        get() = _binding.callNameText.text.toString()
        set(value) {
            _binding.callNameText.text = value
        }

    override var stateText: String?
        get() = _binding.callStateText.text.toString()
        set(value) {
            val old = _binding.callStateText.text.toString()
            _binding.callStateText.text = value
            if (old != value) {
                component.animations.focus(_binding.callStateText)
            }
        }

    override var stateTextColor: Int
        get() = _binding.callStateText.currentTextColor
        set(value) {
            _binding.callStateText.setTextColor(value)
        }

    override var isHoldEnabled: Boolean
        get() = _binding.callActions.isHoldEnabled
        set(value) {
            _binding.callActions.isHoldEnabled = value
        }

    override var isMuteEnabled: Boolean
        get() = _binding.callActions.isMuteEnabled
        set(value) {
            _binding.callActions.isMuteEnabled = value
        }

    override var isSwapEnabled: Boolean
        get() = _binding.callActions.isSwapEnabled
        set(value) {
            _binding.callActions.isSwapEnabled = value
        }

    override var isMergeEnabled: Boolean
        get() = _binding.callActions.isMergeEnabled
        set(value) {
            _binding.callActions.isMergeEnabled = value
        }

    override var isSpeakerEnabled: Boolean
        get() = _binding.callActions.isSpeakerEnabled
        set(value) {
            _binding.callActions.isSpeakerEnabled = value
        }

    override var isMuteActivated: Boolean
        get() = _binding.callActions.isMuteActivated
        set(value) {
            _binding.callActions.isMuteActivated = value
        }

    override var isHoldActivated: Boolean
        get() = _binding.callActions.isHoldActivated
        set(value) {
            _binding.callActions.isHoldActivated = value
        }

    override var isSpeakerActivated: Boolean
        get() = _binding.callActions.isSpeakerActivated
        set(value) {
            _binding.callActions.isSpeakerActivated = value
        }

    override var isBluetoothActivated: Boolean
        get() = _binding.callActions.isBluetoothActivated
        set(value) {
            _binding.callActions.isBluetoothActivated = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        _presenter = CallController(this)
        _binding.apply {
            callActions.setCallActionsListener(_presenter)
            callAnswerButton.setOnClickListener { _presenter.onAnswerClick() }
            callRejectButton.setOnClickListener { _presenter.onRejectClick() }
        }
    }

    override fun showDialpad() {
        BottomFragment(DialpadFragment.newInstance(false).apply {
            setOnKeyDownListener(_presenter::onKeypadKey)
        }).show(supportFragmentManager, DialpadFragment.TAG)
    }

    override fun showActiveCallUI() {
        showActiveLayout()
        _binding.callActions.showSingleCallUI()
    }

    override fun showAddCallDialog() {
        BottomFragment(DialpadFragment.newInstance(true)).show(
            supportFragmentManager,
            DialpadFragment.TAG
        )
    }

    override fun showIncomingCallUI() {
        transitionLayoutTo(R.id.constraint_set_incoming_call)
    }

    override fun showMultiActiveCallUI() {
        showActiveLayout()
        _binding.callActions.showMultiCallUI()
    }

    override fun setElapsedTime(duration: Long?) {
        duration?.let {
            component.animations.show(_binding.callTimeText, true)
            _binding.callTimeText.text = DateUtils.formatElapsedTime(duration / 1000)
        } ?: run {
            component.animations.hide(
                _binding.callTimeText,
                ifVisible = true, goneOrInvisible = false
            )
        }
    }

    override fun showHoldingBanner(number: String) {
        _binding.callBanner.text = number
        if (_binding.callBanner.visibility != View.VISIBLE) {
            _binding.callBanner.visibility = View.VISIBLE
            component.animations.show(_binding.callBanner, true)
            component.animations.focus(_binding.callBanner)
        }
    }

    override fun hideHoldingBanner() {
        component.animations.hide(_binding.callBanner, ifVisible = true, goneOrInvisible = false)
    }


    private fun showActiveLayout() {
        transitionLayoutTo(R.id.constraint_set_active_call)
        if (_binding.callActions.visibility != View.VISIBLE) {
            component.animations.show(_binding.callActions, true)
        }
    }

    private fun transitionLayoutTo(constraintRes: Int) {
        if (_binding.root.currentState != constraintRes) {
            _binding.root.setTransition(_binding.root.currentState, constraintRes)
            _binding.root.transitionToEnd()
        }
    }
}
