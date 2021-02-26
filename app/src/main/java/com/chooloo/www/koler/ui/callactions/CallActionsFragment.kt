package com.chooloo.www.koler.ui.callactions

import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.chooloo.www.koler.databinding.FragmentCallActionsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment

class CallActionsFragment : BaseFragment(), CallActionsMvpView {
    private val _binding by lazy { FragmentCallActionsBinding.inflate(layoutInflater) }
    private val _presenter by lazy { CallActionsPresenter<CallActionsMvpView>() }
    private val _bottomDialpadFragment by lazy { BottomFragment(DialpadFragment.newInstance(false)) }
    private val _audioManager by lazy { _activity.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager }

    companion object {
        fun newInstance() = CallActionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _presenter.attach(this)

        _binding.apply {
            callActionHold.setOnClickListener { _presenter.onHoldClick() }
            callActionAddCall.setOnClickListener { _presenter.onAddCallClick() }
            callActionKeypad.setOnClickListener { _presenter.onKeypadClick() }
            callActionMute.setOnClickListener { _presenter.onMuteClick() }
            callActionRecord.setOnClickListener { _presenter.onRecordClick() }
            callActionSpeaker.setOnClickListener { _presenter.onSpeakerClick() }
        }

        _bottomDialpadFragment.fragment.setOnKeyDownListener(_presenter::onKeypadKey)
    }

    override fun addCall() {
        TODO("Not yet implemented")
    }

    override fun openDialpad() {
        _bottomDialpadFragment.show(childFragmentManager, DialpadFragment.TAG)
    }

    override fun startRecording() {
        TODO("Not yet implemented")
    }

    override fun stopRecording() {
        TODO("Not yet implemented")
    }

    override fun toggleSpeaker(isSpeaker: Boolean) {
        _audioManager.isSpeakerphoneOn = isSpeaker
    }

    override fun toggleMute(isMute: Boolean) {
        _audioManager.isMicrophoneMute = isMute
    }
}