package com.chooloo.www.chooloolib.ui.prompt

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.PromptBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PromptFragment @Inject constructor() : BaseFragment<PromptViewState>() {
    private var _onItemClickListener: (Boolean) -> Boolean = { true }

    override val contentView by lazy { binding.root }
    override val viewState: PromptViewState by viewModels()

    private val binding by lazy { PromptBinding.inflate(layoutInflater) }


    override fun onSetup() {
        binding.apply {

            promptButtonNo.setOnClickListener {
                viewState.onActivated(!_onItemClickListener.invoke(false))
            }

            promptButtonYes.setOnClickListener {
                viewState.onActivated(_onItemClickListener.invoke(true))
            }
        }

        viewState.apply {
            onTitle(args.getString(ARG_TITLE))
            onSubtitle(args.getString(ARG_SUBTITLE))
            onActivated(args.getBoolean(ARG_IS_ACTIVATED, true))


            title.observe(this@PromptFragment) {
                binding.promptTitle.text = it
            }

            subtitle.observe(this@PromptFragment) {
                binding.promptSubtitle.text = it
            }

            isActivated.observe(this@PromptFragment) {
                binding.promptButtonNo.isActivated = !it
                binding.promptButtonYes.isActivated = it
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: (Boolean) -> Boolean) {
        _onItemClickListener = onItemClickListener
    }


    companion object {
        const val ARG_TITLE = "title"
        const val ARG_SUBTITLE = "subtitle"
        const val ARG_IS_ACTIVATED = "is_activated"

        fun newInstance(title: String, subtitle: String, isActivated: Boolean) =
            PromptFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_SUBTITLE, subtitle)
                    putBoolean(ARG_IS_ACTIVATED, isActivated)
                }
            }
    }
}