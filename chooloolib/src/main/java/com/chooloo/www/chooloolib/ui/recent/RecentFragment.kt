package com.chooloo.www.chooloolib.ui.recent

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.RecentBinding
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.recents.RecentsHistoryViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentFragment @Inject constructor() : BaseFragment<RecentViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: RecentViewState by viewModels()

    private val historyViewState: RecentsHistoryViewState by viewModels()
    private val binding by lazy { RecentBinding.inflate(layoutInflater) }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var dialogs: DialogsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var callNavigations: CallNavigationsInteractor


    override fun onSetup() {
        binding.apply {
            recentButtonSms.setOnClickListener {
                viewState.onActionSms()
            }

            recentButtonCall.setOnClickListener {
                viewState.onActionCall()
            }

            recentButtonDelete.setOnClickListener {
                viewState.onActionDelete()
            }

            recentButtonContact.setOnClickListener {
                viewState.onActionOpenContact()
            }

            recentButtonAddContact.setOnClickListener {
                viewState.onActionAddContact()
            }

            recentButtonShowHistory.setOnClickListener {
                viewState.onActionShowHistory()
            }

            recentButtonBlock.setOnClickListener {
                viewState.onActionBlock(!viewState.isBlockButtonActivated.value!!)
            }
        }

        viewState.apply {
            recentId.value = args.getLong(ARG_RECENT_ID)
            imageUri.observe(this@RecentFragment, binding.recentContactImage::setImageURI)
            typeImage.observe(this@RecentFragment, binding.recentTypeImage::setImageDrawable)

            name.observe(this@RecentFragment) {
                binding.recentTextName.text = it
            }

            timeString.observe(this@RecentFragment) {
                binding.recentTextTime.text = it
            }

            durationString.observe(this@RecentFragment) {
                it?.let { binding.recentTextDuration.text = it } ?: run {
                    binding.recentTextDuration.isVisible = false
                    binding.recentImageDuration.isVisible = false
                }
            }

            isContactVisible.observe(this@RecentFragment) {
                binding.recentButtonContact.isVisible = it
            }

            isAddContactVisible.observe(this@RecentFragment) {
                binding.recentButtonAddContact.isVisible = it
            }

            isBlockButtonVisible.observe(this@RecentFragment) {
                binding.recentButtonBlock.isVisible = it
            }

            isBlockButtonActivated.observe(this@RecentFragment) {
                binding.recentButtonBlock.isActivated = it
            }


            callEvent.observe(this@RecentFragment) { ev ->
                ev.ifNew?.let(callNavigations::call)
            }

            showContactEvent.observe(this@RecentFragment) { ev ->
                ev.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getBriefContactFragment(it))
                }
            }

            showRecentEvent.observe(this@RecentFragment) { ev ->
                ev.ifNew?.let { prompts.showFragment(fragmentFactory.getRecentFragment(it)) }
            }

            showHistoryEvent.observe(this@RecentFragment) { ev ->
                ev.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getRecentsHistoryFragment(it))
                }
            }

            confirmRecentDeleteEvent.observe(this@RecentFragment) {
                it.ifNew?.let {
                    dialogs.askForValidation(R.string.explain_delete_recent) { result ->
                        if (result) viewState.onConfirmDelete()
                    }
                }
            }
        }

        historyViewState.itemClickedEvent.observe(this) {
            it.ifNew?.let { recent -> viewState.onRecentClick(recent.id) }
        }
    }

    companion object {
        const val ARG_RECENT_ID = "recent_id"

        fun newInstance(recentId: Long) = RecentFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_RECENT_ID, recentId)
            }
        }
    }
}