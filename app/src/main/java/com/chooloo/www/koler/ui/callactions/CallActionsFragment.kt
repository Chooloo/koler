package com.chooloo.www.koler.ui.callactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chooloo.www.koler.call.CallRecorder
import com.chooloo.www.koler.databinding.CallActionsBinding
import com.chooloo.www.koler.interactor.audio.AudioInteractor.AudioMode.IN_CALL
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment

class CallActionsFragment : BaseFragment(), CallActionsContract.View {
    private val _presenter by lazy { CallActionsPresenter(this) }
    private val _binding by lazy { CallActionsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onSetup() {
        _binding.apply {
            callActionHold.setOnClickListener { _presenter.onHoldClick() }
            callActionAddCall.setOnClickListener { _presenter.onAddCallClick() }
            callActionKeypad.setOnClickListener { _presenter.onKeypadClick() }
            callActionMute.setOnClickListener { _presenter.onMuteClick() }
            callActionRecord.setOnClickListener { _presenter.onRecordClick() }
            callActionSpeaker.setOnClickListener { _presenter.onSpeakerClick() }
        }
    }

    override fun openDialpad() {
        BottomFragment(DialpadFragment.newInstance(false).apply {
            setOnKeyDownListener(_presenter::onKeypadKey)
        }).show(childFragmentManager, DialpadFragment.TAG)
    }


    companion object {
        fun newInstance() = CallActionsFragment()
    }
}