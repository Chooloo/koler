package com.chooloo.www.koler.ui.call

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import com.chooloo.www.koler.R
import com.chooloo.www.koler.call.CallManager
import com.chooloo.www.koler.databinding.CallBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.callactions.CallActionsFragment

@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity(), CallContract.View {
    private lateinit var _presenter: CallPresenter<CallActivity>
    private val _binding by lazy { CallBinding.inflate(layoutInflater) }
    private val _callListener by lazy {
        object : CallManager.CallListener(this) {
            override fun onCallDetailsChanged(callDetails: CallDetails) {
                _presenter.onCallDetailsChanged(callDetails)
            }
        }
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        _presenter = CallPresenter(this)
        _binding.apply {
            callAnswerButton.setOnClickListener { _presenter.onAnswerClick() }
            callRejectButton.setOnClickListener { _presenter.onRejectClick() }
        }
        CallManager.registerListener(_callListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        CallManager.unregisterCallback(_callListener)
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
            boundComponent.animationInteractor.animateIn(_binding.callActionsContainer)
            _binding.root.transitionToEnd()
        }
    }

    override fun animateStateTextAttention() {
        boundComponent.animationInteractor.animateFocus(_binding.callStateText)
    }

    //endregion
}
