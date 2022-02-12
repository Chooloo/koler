package com.chooloo.www.chooloolib.ui.widgets.listitem

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor

class ListItemButton : ListItem {
    private val colorSecondary by lazy { context.getAttrColor(R.attr.colorSecondary) }
    private val colorOnSecondary by lazy { context.getAttrColor(R.attr.colorOnSecondary) }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        image.setPadding(15)
        stateListAnimator = null
        imageTintList = ColorStateList.valueOf(colorOnSecondary)
        personLayout.backgroundTintList = ColorStateList.valueOf(colorSecondary)
        personLayout.background = ContextCompat.getDrawable(context, R.drawable.bubble_background)

        setTitleColor(colorOnSecondary)
    }

    override fun setPaddingMode(isCompact: Boolean, isEnabled: Boolean) {
        personLayout.setPadding(
            dimenSpacingSmall,
            dimenSpacing - 28,
            dimenSpacingSmall,
            dimenSpacing - 28
        )
        header.setPadding(
            if (isEnabled) dimenSpacing else 0,
            dimenSpacingSmall,
            if (isEnabled) dimenSpacing else 0,
            if (isCompact) dimenSpacingSmall - 10 else dimenSpacingSmall
        )
    }
}