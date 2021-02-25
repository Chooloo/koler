package com.chooloo.www.koler.ui.callactions

import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.koler.databinding.FragmentCallActionsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.util.call.CallManager

class CallActionsFragment : BaseFragment(), CallActionsMvpView {
    private val _binding by lazy { FragmentCallActionsBinding.inflate(layoutInflater) }
    private val _bottomDialpadFragment by lazy { BottomFragment(DialpadFragment.newInstance(false)) }
    private val _audioManager by lazy { _activity.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager }

    companion object {
        const val TAG = "call_actions_fragment"

        fun newInstance() = CallActionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return _binding.root
    }

    override fun onSetup() {
    }

    override fun addCall() {
        TODO("Not yet implemented")
    }

    override fun openDialpad() {
        _bottomDialpadFragment.show(_activity.supportFragmentManager, DialpadFragment.TAG)
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