package com.chooloo.www.chooloolib.ui.prompt

import android.os.Bundle
import com.chooloo.www.chooloolib.databinding.PromptBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PromptFragment @Inject constructor() : BaseFragment(), PromptContract.View {
    override val contentView by lazy { binding.root }
    override lateinit var controller: PromptContract.Controller

    private var _onClickListener: ((result: Boolean) -> Unit)? = null
    private val binding by lazy { PromptBinding.inflate(layoutInflater) }


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
        controller = controllerFactory.getPromptController(this)
        binding.apply {
            promptTitle.text = args.getString(ARG_TITLE)
            promptSubtitle.text = args.getString(ARG_SUBTITLE)
            promptButtonNo.setOnClickListener { controller.onNoClick() }
            promptButtonYes.setOnClickListener { controller.onYesClick() }
        }
        _onClickListener?.let(controller::setOnClickListener)
    }

    fun setOnClickListener(onClickListener: (result: Boolean) -> Unit) {
        _onClickListener = onClickListener
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