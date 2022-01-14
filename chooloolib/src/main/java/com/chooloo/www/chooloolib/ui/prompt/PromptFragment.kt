package com.chooloo.www.chooloolib.ui.prompt

import android.os.Bundle
import com.chooloo.www.chooloolib.databinding.PromptBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment

class PromptFragment : BaseFragment(), PromptContract.View {
    val controller by lazy { PromptController(this) }
    private val binding by lazy { PromptBinding.inflate(layoutInflater) }

    override val contentView by lazy { binding.root }

    override var title: String?
        get() = binding.promptTitle.text.toString()
        set(value) {
            binding.promptTitle.text = value
        }

    override var subtitle: String?
        get() = binding.promptSubtitle.text.toString()
        set(value) {
            binding.promptSubtitle.text = value
        }


    override fun onSetup() {
        binding.apply {
            promptTitle.text = args.getString(ARG_TITLE)
            promptSubtitle.text = args.getString(ARG_SUBTITLE)
            promptButtonNo.setOnClickListener { controller.onNoClick() }
            promptButtonYes.setOnClickListener { controller.onYesClick() }
        }
    }


    companion object {
        const val ARG_TITLE = "title"
        const val TAG = "prompt_fragment"
        const val ARG_SUBTITLE = "subtitle"

        fun newInstance(title: String, subtitle: String) = PromptFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_SUBTITLE, subtitle)
            }
        }
    }
}