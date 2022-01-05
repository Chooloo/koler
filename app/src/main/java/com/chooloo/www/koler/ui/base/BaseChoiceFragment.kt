package com.chooloo.www.koler.ui.base

import android.os.Bundle
import com.chooloo.www.koler.adapter.ChoiceAdapter
import com.chooloo.www.koler.databinding.MenuBinding

class BaseChoiceFragment : BaseFragment() {
    private var _onChoiceClickListener: (String) -> Unit = {}
    private val adapter by lazy { ChoiceAdapter(component) }
    private val binding by lazy { MenuBinding.inflate(layoutInflater) }

    override val contentView by lazy { binding.root }


    override fun onSetup() {
        adapter.setOnItemClickListener(_onChoiceClickListener::invoke)
        binding.apply {
            menuRecyclerView.adapter = adapter
            menuTitle.text = args.getString(ARG_TITLE)
        }
    }


    fun setOnChoiceClickListener(onChoiceClickListener: (String) -> Unit) {
        _onChoiceClickListener = onChoiceClickListener
    }


    companion object {
        const val ARG_TITLE = "title"

        fun newInstance(title: String) = BaseChoiceFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
            }
        }
    }
}