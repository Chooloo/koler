package com.chooloo.www.chooloolib.ui.prompt

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.PromptBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PromptFragment @Inject constructor() : BaseFragment<PromptViewState>() {
    private var _onItemClickListener: (Boolean) -> Unit = {}

    override val contentView by lazy { binding.root }
    override val viewState: PromptViewState by viewModels()

    private val binding by lazy { PromptBinding.inflate(layoutInflater) }


    override fun _onSetup() {
        binding.apply {
            viewState.title.value = args.getString(ARG_TITLE)
            viewState.subtitle.value = args.getString(ARG_SUBTITLE)

            promptButtonNo.setOnClickListener {
                viewState.onNoClick()
                _onItemClickListener.invoke(false)
            }

            promptButtonYes.setOnClickListener {
                viewState.onYesClick()
                _onItemClickListener.invoke(true)
            }
        }

        viewState.apply {
            title.observe(this@PromptFragment) {
                binding.promptTitle.text = it
            }

            subtitle.observe(this@PromptFragment) {
                binding.promptSubtitle.text = it
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: (Boolean) -> Unit) {
        _onItemClickListener = onItemClickListener
    }


    companion object {
        const val ARG_TITLE = "title"
        const val ARG_SUBTITLE = "subtitle"

        fun newInstance(title: String, subtitle: String) = PromptFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_SUBTITLE, subtitle)
            }
        }
    }
}