package com.chooloo.www.koler.ui.call

import android.media.AudioManager
import android.media.AudioManager.MODE_IN_CALL
import android.net.Uri
import android.os.Bundle
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
        _binding.root.transitionToEnd()
    }
}