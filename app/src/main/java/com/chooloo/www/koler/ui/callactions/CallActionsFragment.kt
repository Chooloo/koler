package com.chooloo.www.koler.ui.callactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chooloo.www.koler.databinding.FragmentCallActionsBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.util.audio.AudioManager
import com.chooloo.www.koler.util.audio.AudioManager.AudioMode.IN_CALL
import com.chooloo.www.koler.util.callrecord.CallRecorder

class CallActionsFragment : BaseFragment(), CallActionsContract.View {
    private val _callRecorder by lazy { CallRecorder(_activity) }
    private val _audioManager by lazy { AudioManager(requireContext()) }
    private val _presenter by lazy { CallActionsPresenter<CallActionsContract.View>() }
    private val _binding by lazy { FragmentCallActionsBinding.inflate(layoutInflater) }
    private val _bottomDialpadFragment by lazy { BottomFragment(DialpadFragment.newInstance(false)) }

    companion object {
        fun newInstance() = CallActionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _audioManager.audioMode = IN_CALL
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

    override fun onDestroy() {
        super.onDestroy()
        _callRecorder.stopRecording()
    }

    override fun addCall() {
        showError("Feature in development")
    }

    override fun openDialpad() {
        _bottomDialpadFragment.show(childFragmentManager, DialpadFragment.TAG)
    }

    override fun stopRecording() {
        _callRecorder.stopRecording().also {
            showMessage("Finished recording at ${it?.filename}")
        }
    }

    override fun startRecording() {
        showError("Feature in development")
//        CallManager.sCall?.let {
//            _callRecorder.startRecording(
//                CallDetails.fromCall(it, _activity).contact.number ?: "Unknown"
//            )
//            blinkView(_binding.callActionRecord, 2000)
//        }
    }

    override fun toggleMute(isMute: Boolean) {
        _audioManager.isMuted = isMute
    }

    override fun toggleSpeaker(isSpeaker: Boolean) {
        _audioManager.isSpeakerOn = isSpeaker
    }
}