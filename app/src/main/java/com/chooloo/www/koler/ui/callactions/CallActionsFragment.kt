package com.chooloo.www.koler.ui.callactions

import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.koler.databinding.FragmentCallActionsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.dialpad.DialpadBottomFragment
import com.chooloo.www.koler.util.call.CallManager

class CallActionsFragment : BaseFragment(), CallActionsMvpView {
    private val _binding by lazy { FragmentCallActionsBinding.inflate(layoutInflater) }
    private val _bottomDialpadFragment by lazy { DialpadBottomFragment.newInstance(false) }
    private val _audioManager by lazy { _activity.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager }

    companion object {
        fun newInstance() = CallActionsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return _binding.root
    }

    override fun onSetup() {
    }

    override fun addCall() {
        TODO("Not yet implemented")
    }

    override fun openKeypad() {
        _bottomDialpadFragment.show(_activity.supportFragmentManager, _bottomDialpadFragment.tag)
    }

    override fun startRecording() {
        TODO("Not yet implemented")
    }

    override fun stopRecording() {
        TODO("Not yet implemented")
    }

    override fun toggleHold(isHold: Boolean) {
        CallManager.hold(isHold)
    }

    override fun toggleSpeaker(isSpeaker: Boolean) {
        _audioManager.isSpeakerphoneOn = isSpeaker
    }

    override fun toggleMute(isMute: Boolean) {
        _audioManager.isMicrophoneMute = isMute
    }

    override fun applyCallKeypad(keyChar: Char) {
        CallManager.keypad(keyChar)
    }
}