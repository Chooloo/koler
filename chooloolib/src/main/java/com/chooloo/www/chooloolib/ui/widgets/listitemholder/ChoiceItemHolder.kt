package com.chooloo.www.chooloolib.ui.widgets.listitemholder

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.ListItemBinding
import com.google.android.material.internal.ViewUtils

class ChoiceItemHolder(binding: ListItemBinding) : ListItemHolder(binding) {
    val dimenSpacingSmall by lazy { context.resources.getDimensionPixelSize(R.dimen.default_spacing_small) }
    val dimenSpacingMedium by lazy { context.resources.getDimensionPixelSize(R.dimen.default_spacing_medium) }

    init {
        isImageVisible = false
        isRightButtonVisible = true

        setRightButtonIcon(R.drawable.chevron_right)
        binding.root.setPadding(dimenSpacingSmall, dimenSpacingSmall, dimenSpacingSmall, dimenSpacingSmall)
        binding.listItemMainLayout.strokeWidth = ViewUtils.dpToPx(context, 1).toInt()
        binding.listItemMainConstraintLayout.setPadding(dimenSpacingSmall, dimenSpacingSmall, 0, dimenSpacingSmall)
    }
}