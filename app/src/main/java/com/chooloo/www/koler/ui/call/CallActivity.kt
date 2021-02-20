package com.chooloo.www.koler.ui.call

import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.telecom.Call
import android.telecom.Call.Details
import android.view.View.VISIBLE
import com.chooloo.www.koler.databinding.ActivityCallBinding
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.callactions.CallActionsBottomFragment
import com.chooloo.www.koler.util.ProximitySensor
import com.chooloo.www.koler.util.call.CallManager
import com.chooloo.www.koler.util.disableKeyboard
import com.chooloo.www.koler.util.setShowWhenLocked

class CallActivity : BaseActivity(), CallMvpView {

    private val _callCallback by lazy { CallCallback() }
    private val _proximitySensor by lazy { ProximitySensor(this) }
    private val _binding by lazy { ActivityCallBinding.inflate(layoutInflater) }
    private val _presenter: CallMvpPresenter<CallMvpView> by lazy { CallPresenter() }
    private val _bottomCallActionsFragment by lazy { CallActionsBottomFragment.newInstance() }
    private val _audioManager by lazy { applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager }

    override var status: String?
        get() = _binding.callStatusText.text.toString()
        set(value) {
            _binding.callStatusText.text = value
        }

    override var callerName: String?
        get() = _binding.callNameText.text.toString()
        set(value) {
            _binding.callNameText.text = value
        }

    override var callerImage:Uri?
        get() = null
        set(value) {
            _binding.callImage.setImageURI(value)
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
            callActionsButton.setOnClickListener { _presenter.onCallActionsClick() }
        }

        disableKeyboard()
        setShowWhenLocked()
        CallManager.registerCallback(_callCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
        _proximitySensor.release()
        CallManager.unregisterCallback(_callCallback)
    }

    override fun answer() {
        CallManager.answer()
    }

    override fun reject() {
        CallManager.reject()
    }

    override fun setAudioInCall() {
        _audioManager.mode = AudioManager.MODE_IN_CALL
    }

    override fun openCallActions() {
        _bottomCallActionsFragment.show(supportFragmentManager, CallActionsBottomFragment.TAG)
    }

    override fun lookupContact(number: String): Contact {
        return lookupContact(number)
    }

    inner class CallCallback : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            super.onStateChanged(call, state)
            _presenter.onStateChanged(state)
        }

        override fun onDetailsChanged(call: Call, details: Details) {
            super.onDetailsChanged(call, details)
            _presenter.onDetailsChanged(details)
        }
    }
}