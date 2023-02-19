package com.chooloo.www.chooloolib.ui.widgets.listitemholder

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.ListItemBinding
import com.chooloo.www.chooloolib.util.getAttrColor

class ChoiceItemHolder(binding: ListItemBinding) : ListItemHolder(binding) {
    val dimenSpacingSmall by lazy { context.resources.getDimensionPixelSize(R.dimen.default_spacing_small) }
    private val _defaultBackgroundColor: Int
    private val _defaultTextColor: Int

    init {
        isImageVisible = false

        _defaultTextColor = context.getAttrColor(R.attr.colorOnBackgroundVariant)
        _defaultBackgroundColor = context.getAttrColor(R.attr.colorBackgroundVariant)

        binding.root.setPadding(dimenSpacing, dimenSpacingSmall, dimenSpacing, dimenSpacingSmall)
        binding.listItemTitle.setTextColor(_defaultTextColor)
        binding.listItemMainLayout.setCardBackgroundColor(_defaultBackgroundColor)
        binding.listItemMainConstraintLayout.setPadding(
            dimenSpacing,
            dimenSpacing,
            0,
            dimenSpacing
        )
    }

    fun setSelected(isSelected: Boolean) {
        binding.listItemMainLayout.setCardBackgroundColor(if (isSelected) context.getAttrColor(R.attr.colorSecondaryContainer) else _defaultBackgroundColor)
        binding.listItemTitle.setTextColor(if (isSelected) context.getAttrColor(R.attr.colorOnSecondaryContainer) else _defaultTextColor)
    }
}