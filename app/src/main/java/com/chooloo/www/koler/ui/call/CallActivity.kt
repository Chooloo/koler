package com.chooloo.www.koler.ui.call

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.View.VISIBLE
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.CallBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.callactions.CallActionsFragment
import com.chooloo.www.koler.ui.calls.CallsFragment
import com.chooloo.www.koler.util.AnimationManager
import com.chooloo.www.koler.util.ProximitySensor
import com.chooloo.www.koler.util.call.CallItem
import com.chooloo.www.koler.util.disableKeyboard
import com.chooloo.www.koler.util.setShowWhenLocked

@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity(), CallContract.View {
    private val _binding by lazy { CallBinding.inflate(layoutInflater) }
    private val _presenter by lazy { CallPresenter<CallContract.View>() }
    private val _proximitySensor by lazy { ProximitySensor(this) }
    private val _animationManager by lazy { AnimationManager(this) }
    private val _secondaryCallsFragment by lazy { CallsFragment.newInstance(true) }

    companion object {
        private const val SECONDARY_CALLS_FRAGMENT_TAG = "secondary_calls_fragment"
    }

    override var stateText: String?
        get() = _binding.callStateText.text.toString()
        set(value) {
            _binding.callStateText.text = value
        }

    override var stateTextColor: Int
        get() = _binding.callStateText.currentTextColor
        set(value) {
            _binding.callStateText.setTextColor(value)
        }

    override var callerNameText: String?
        get() = _binding.callNameText.text.toString()
        set(value) {
            _binding.callNameText.text = value
        }

    override var callerImageURI: Uri?
        get() = null
        set(value) {
            _binding.callImage.setImageURI(value)
        }

    override var secondaryCallsVisibility: Boolean
        get() = _binding.callListContainer.visibility == VISIBLE
        set(value) {
            _animationManager.showView(_binding.callListContainer, value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        _presenter.attach(this)
        _proximitySensor.acquire()

        _binding.apply {
            callAnswerButton.setOnClickListener { _presenter.onAnswerClick() }
            callRejectButton.setOnClickListener { _presenter.onRejectClick() }
        }

        _presenter.onDisplayCalls()
        disableKeyboard()
        setShowWhenLocked()
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
        _proximitySensor.release()
    }

    override fun blinkStateText() {
        _animationManager.blinkView(_binding.callStateText, 2500)
    }

    override fun startStopwatch() {
        _binding.callChronometer.apply {
            base = SystemClock.elapsedRealtime()
            start()
        }
    }

    override fun stopStopwatch() {
        _binding.callChronometer.stop()
    }

    override fun transitionToActiveUI() {
        if (_binding.root.currentState == R.id.incoming_call) {
            supportFragmentManager
                .beginTransaction()
                .add(_binding.callActionsContainer.id, CallActionsFragment.newInstance())
                .commitNow()
            _animationManager.showView(_binding.callActionsContainer, true)
            _binding.root.transitionToEnd()
        }
    }

    override fun updateSecondaryCall(callItem: CallItem) {
        attachSecondaryCallsFragment()
        _secondaryCallsFragment.updateCallItem(callItem)
        if (_secondaryCallsFragment.itemCount == 0) {
            secondaryCallsVisibility = false
        } else if (!secondaryCallsVisibility) {
            secondaryCallsVisibility = true
        }
    }

    override fun getCallAccount(callItem: CallItem) = callItem.getAccount(this)

    private fun attachSecondaryCallsFragment() {
        if (supportFragmentManager.findFragmentByTag(SECONDARY_CALLS_FRAGMENT_TAG) == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    _binding.callListContainer.id,
                    _secondaryCallsFragment,
                    SECONDARY_CALLS_FRAGMENT_TAG
                )
                .commitNow()
            _animationManager.showView(_binding.callListContainer, true)
        }
    }
}
