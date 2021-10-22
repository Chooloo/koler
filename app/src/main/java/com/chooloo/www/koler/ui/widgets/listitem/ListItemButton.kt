package com.chooloo.www.koler.ui.widgets.listitem

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.ViewManager

class ListItemButton : ListItem {
    private val _viewManager by lazy { ViewManager(context) }

    private val colorSecondary by lazy { _viewManager.getAttrColor(R.attr.colorSecondary) }
    private val colorOnSecondary by lazy { _viewManager.getAttrColor(R.attr.colorOnSecondary) }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        stateListAnimator = null
        background = ContextCompat.getDrawable(context, R.drawable.bubble_background)
        backgroundTintList = ColorStateList.valueOf(colorSecondary)

        setTitleColor(colorOnSecondary)
        setPadding(dimenSpacing, 0, dimenSpacing, 0)
    }

    override fun setPaddingMode(isCompact: Boolean, isEnabled: Boolean) {
        _personLayout.setPadding(
            0,
            dimenSpacing - 28,
            0,
            dimenSpacing - 28
        )
        _header.setPadding(
            if (isEnabled) dimenSpacing else 0,
            dimenSpacingSmall,
            if (isEnabled) dimenSpacing else 0,
            if (isCompact) dimenSpacingSmall - 10 else dimenSpacingSmall
        )
    }
}