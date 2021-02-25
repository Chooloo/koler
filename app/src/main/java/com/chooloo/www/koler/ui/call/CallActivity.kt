package com.chooloo.www.koler.ui.call

import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import com.chooloo.www.koler.databinding.ActivityCallBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.callactions.CallActionsFragment
import com.chooloo.www.koler.util.ProximitySensor
import com.chooloo.www.koler.util.disableKeyboard
import com.chooloo.www.koler.util.lookupContact
import com.chooloo.www.koler.util.setShowWhenLocked

class CallActivity : BaseActivity(), CallMvpView {
    private val _proximitySensor by lazy { ProximitySensor(this) }
    private val _binding by lazy { ActivityCallBinding.inflate(layoutInflater) }
    private val _presenter: CallMvpPresenter<CallMvpView> by lazy { CallPresenter() }
    private val _callActionsFragment by lazy { CallActionsFragment.newInstance() }
    private val _audioManager by lazy { applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager }

    override var stateText: String?
        get() = _binding.callStatusText.text.toString()
        set(value) {
            _binding.callStatusText.text = value
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

        supportFragmentManager
            .beginTransaction()
            .replace(_binding.callActionsPlaceholder.id, _callActionsFragment)
            .commitNow()

        _binding.apply {
            callAnswerButton.setOnClickListener { _presenter.onAnswerClick() }
            callRejectButton.setOnClickListener { _presenter.onRejectClick() }
            callActionsPlaceholder.visibility = GONE
        }

        disableKeyboard()
        setShowWhenLocked()

        _presenter.onInitialUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
        _proximitySensor.release()
    }

    override fun setAudioInCall() {
        _audioManager.mode = AudioManager.MODE_IN_CALL
    }

    override fun getContact(number: String) = lookupContact(number)

    override fun switchToActiveCallUI() {
        _binding.root.transitionToEnd()
    }
}