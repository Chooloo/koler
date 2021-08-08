package com.chooloo.www.koler.ui.call

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.CallDetails
import com.chooloo.www.koler.databinding.CallBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.callactions.CallActionsFragment
import com.chooloo.www.koler.util.AnimationManager
import com.chooloo.www.koler.util.ProximitySensor
import com.chooloo.www.koler.util.ScreenManager
import com.chooloo.www.koler.call.CallManager
import com.chooloo.www.koler.util.call.getNumber
import com.chooloo.www.koler.call.CallRecorder

@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity(), CallContract.View {
    private val _screenManager by lazy { ScreenManager(this) }
    private val _binding by lazy { CallBinding.inflate(layoutInflater) }
    private val _proximitySensor by lazy { ProximitySensor(this) }
    private val _animationManager by lazy { AnimationManager(this) }
    private val _presenter by lazy { CallPresenter<CallContract.View>(this) }
    private val _callListener by lazy {
        object : CallManager.CallListener(this) {
            override fun onCallDetailsChanged(callDetails: CallDetails) {
                _presenter.onCallDetailsChanged(callDetails)
            }
        }
    }

    private val _callRecorder by lazy { CallRecorder(this) }


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        _proximitySensor.acquire()

        _binding.apply {
            callAnswerButton.setOnClickListener { _presenter.onAnswerClick() }
            callRejectButton.setOnClickListener { _presenter.onRejectClick() }
        }

        _screenManager.disableKeyboard()
        _screenManager.setShowWhenLocked()
        CallManager.registerListener(_callListener)

        CallManager.sCall?.getNumber()?.let { _callRecorder.initRecording(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        CallManager.unregisterCallback(_callListener)
        _proximitySensor.release()
        _callRecorder.stopRecording()
    }


    //region call view

    override fun stopStopwatch() {
        _binding.callChronometer.stop()
    }

    override fun startStopwatch() {
        _binding.callChronometer.apply {
            base = SystemClock.elapsedRealtime()
            start()
        }
    }

    override fun transitionToActiveUI() {
        if (_binding.root.currentState == R.id.incoming_call) {
            supportFragmentManager
                .beginTransaction()
                .add(_binding.callActionsContainer.id, CallActionsFragment.newInstance())
                .commitNow()
            _animationManager.bounceIn(_binding.callActionsContainer)
            _binding.root.transitionToEnd()
        }
    }

    override fun animateStateTextAttention() {
        _animationManager.tada(_binding.callStateText)
    }

    //endregion
}
