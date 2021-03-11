package com.chooloo.www.koler.ui.call

import android.media.AudioManager
import android.media.AudioManager.MODE_IN_CALL
import android.net.Uri
import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.ActivityCallBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.callactions.CallActionsFragment
import com.chooloo.www.koler.util.*

class CallActivity : BaseActivity(), CallContract.View {
    private val _presenter by lazy { CallPresenter<CallContract.View>() }
    private val _proximitySensor by lazy { ProximitySensor(this) }
    private val _binding by lazy { ActivityCallBinding.inflate(layoutInflater) }

    override var stateText: String?
        get() = _binding.callStateText.text.toString()
        set(value) {
            _binding.callStateText.text = value
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
        _presenter.attach(this)
        _proximitySensor.acquire()

        _binding.apply {
            callAnswerButton.setOnClickListener { _presenter.onAnswerClick() }
            callRejectButton.setOnClickListener { _presenter.onRejectClick() }
        }

        disableKeyboard()
        setShowWhenLocked()

        (getSystemService(AUDIO_SERVICE) as AudioManager).mode = MODE_IN_CALL

        _presenter.onInitialUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
        _proximitySensor.release()
    }

    override fun getContact(number: String) = lookupContact(number)

    override fun switchToActiveCallUI() {
        supportFragmentManager
            .beginTransaction()
            .add(_binding.callActionsContainer.id, CallActionsFragment.newInstance())
            .commitNow()
        showView(_binding.callActionsContainer, true)
        _binding.root.transitionToEnd()
    }

    override fun setStateActive() {
        _binding.callStateText.setTextColor(getColor(R.color.green_dark))
        blinkView(_binding.callStateText, 400, 2000)
        _binding.callChronometer.start()
    }

    override fun setStateEnded() {
        _binding.callStateText.setTextColor(getColor(R.color.red_dark))
        blinkView(_binding.callStateText, 400, 3000)
        _binding.callChronometer.stop()
    }
}
