package com.chooloo.www.chooloolib.ui.recent

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.RecentBinding
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.telecom.TelecomInteractor
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.recent.menu.RecentMenuViewState
import com.chooloo.www.chooloolib.ui.recents.RecentsViewState
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentFragment @Inject constructor() : BaseFragment<RecentViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: RecentViewState by viewModels()

    private val historyViewState: RecentsViewState by viewModels()
    private val menuViewState: RecentMenuViewState by activityViewModels()
    private val binding by lazy { RecentBinding.inflate(layoutInflater) }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var telecomInteractor: TelecomInteractor


    override fun onSetup() {
        binding.recentContactImage.isVisible = false

        binding.recentMainActions.apply {
            recentButtonSms.setOnClickListener { viewState.onSms() }
            recentButtonCall.setOnClickListener { viewState.onCall() }
            recentButtonMore.setOnClickListener { viewState.onMoreClick() }
            recentButtonContact.setOnClickListener { viewState.onOpenContact() }
            recentButtonAddContact.setOnClickListener { viewState.onAddContact() }
        }

        viewState.apply {
            onRecentId(args.getLong(ARG_RECENT_ID))
            isBlocked.observe(this@RecentFragment, menuViewState::onIsBlocked)
            recentNumber.observe(this@RecentFragment, menuViewState::onRecentNumber)
            typeImage.observe(this@RecentFragment, binding.recentTypeImage::setImageResource)

            imageUri.observe(this@RecentFragment) {
                binding.recentContactImage.isVisible = true
                Picasso.with(baseActivity).load(it).into(binding.recentContactImage)
            }

            name.observe(this@RecentFragment) {
                binding.recentTextName.text = it
            }

            caption.observe(this@RecentFragment) {
                binding.recentCaption.text = it
            }

            isContactVisible.observe(this@RecentFragment) {
                binding.recentMainActions.recentButtonContact.isVisible = it
            }

            isAddContactVisible.observe(this@RecentFragment) {
                binding.recentMainActions.recentButtonAddContact.isVisible = it
            }

            callEvent.observe(this@RecentFragment) { ev ->
                ev.ifNew?.let(telecomInteractor::callNumber)
            }

            showMoreEvent.observe(this@RecentFragment) {
                it.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getRecentMenuFragment())
                }
            }

            showContactEvent.observe(this@RecentFragment) { ev ->
                ev.ifNew?.let {
                    prompts.showFragment(fragmentFactory.getBriefContactFragment(it))
                }
            }

            showRecentEvent.observe(this@RecentFragment) { ev ->
                ev.ifNew?.let { prompts.showFragment(fragmentFactory.getRecentFragment(it)) }
            }
        }

        historyViewState.itemClickedEvent.observe(this) {
            it.ifNew?.let { recent -> viewState.onRecentClick(recent.id) }
        }

        menuViewState.apply {
            finishEvent.observe(this@RecentFragment) {
                viewState.onFinish()
            }

            onRecentId(args.getLong(ARG_RECENT_ID))
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