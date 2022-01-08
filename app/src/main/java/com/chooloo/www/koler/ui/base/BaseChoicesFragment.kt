package com.chooloo.www.koler.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.chooloo.www.koler.adapter.ChoicesAdapter
import com.chooloo.www.koler.databinding.MenuBinding

open class BaseChoicesFragment : BaseFragment() {
    private var _onChoiceClickListener: (String) -> Unit = {}
    protected open val adapter by lazy { ChoicesAdapter(component) }
    protected open val binding by lazy { MenuBinding.inflate(layoutInflater) }

    override val contentView by lazy { binding.root }


    override fun onSetup() {
        adapter.apply {
            items = args.getStringArrayList(ARG_CHOICES)?.toList() as List<String>
            setOnItemClickListener(_onChoiceClickListener::invoke)
        }
        binding.apply {
            menuRecyclerView.adapter = adapter
            menuTitle.text = getString(args.getInt(ARG_TITLE_RES))
            val subtitleRes = args.getInt(ARG_SUBTITLE_RES, -1)
            if (subtitleRes != -1) {
                menuSubtitle.text = getString(subtitleRes)
            } else {
                menuSubtitle.visibility = View.GONE
            }
        }
    }


    fun setOnChoiceClickListener(onChoiceClickListener: (String) -> Unit) {
        _onChoiceClickListener = onChoiceClickListener
    }


    companion object {
        const val ARG_CHOICES = "choices"
        const val TAG = "choice_fragment"
        const val ARG_TITLE_RES = "title_res"
        const val ARG_SUBTITLE_RES = "subtitle_res"

        fun newInstance(
            @StringRes titleRes: Int,
            @StringRes subtitleRes: Int?,
            choices: List<String>
        ) = BaseChoicesFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_TITLE_RES, titleRes)
                subtitleRes?.let { putInt(ARG_SUBTITLE_RES, it) }
                putStringArrayList(ARG_CHOICES, ArrayList(choices))
            }
        }
    }
}