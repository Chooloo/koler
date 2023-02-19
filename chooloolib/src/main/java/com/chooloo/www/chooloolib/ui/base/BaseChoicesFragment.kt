package com.chooloo.www.chooloolib.ui.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.adapter.ChoicesAdapter
import com.chooloo.www.chooloolib.databinding.MenuBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseChoicesFragment @Inject constructor() : BaseFragment<BaseViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: BaseViewState by viewModels()

    private var _onChoiceSelectedListener: (String) -> Boolean = { true }
    protected open val binding by lazy { MenuBinding.inflate(layoutInflater) }

    @Inject lateinit var adapter: ChoicesAdapter


    override fun onSetup() {
        adapter.apply {
            setOnChoiceSelectedListener(_onChoiceSelectedListener)
            selectedIndex = args.getInt(ARG_SELECTED_INDEX, -1)
            items = args.getStringArrayList(ARG_CHOICES)?.toList() as List<String>
        }

        binding.apply {
            menuRecyclerView.adapter = adapter

            val titleRes = args.getInt(ARG_TITLE_RES, -1)
            if (titleRes != -1) {
                menuTitle.text = getString(titleRes)
                menuTitle.isVisible = true
            }

            val subtitleRes = args.getInt(ARG_SUBTITLE_RES, -1)
            if (subtitleRes != -1) {
                menuSubtitle.text = getString(subtitleRes)
                menuSubtitle.isVisible = true
            }
        }
    }


    fun setOnChoiceClickListener(onChoiceSelectedListener: (String) -> Boolean) {
        _onChoiceSelectedListener = onChoiceSelectedListener
    }


    companion object {
        const val ARG_CHOICES = "choices"
        const val ARG_TITLE_RES = "title_res"
        const val ARG_SUBTITLE_RES = "subtitle_res"
        const val ARG_SELECTED_INDEX = "selected_index"

        fun newInstance(
            @StringRes titleRes: Int,
            @StringRes subtitleRes: Int?,
            choices: List<String>,
            selectedChoiceIndex: Int? = null
        ) = BaseChoicesFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_TITLE_RES, titleRes)
                subtitleRes?.let { putInt(ARG_SUBTITLE_RES, it) }
                putStringArrayList(ARG_CHOICES, ArrayList(choices))
                selectedChoiceIndex?.let { putInt(ARG_SELECTED_INDEX, it) }
            }
        }
    }
}