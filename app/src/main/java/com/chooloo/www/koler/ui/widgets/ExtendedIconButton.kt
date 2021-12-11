package com.chooloo.www.koler.ui.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.R

class ExtendedIconButton : AppCompatButton {
    private val componentRoot by lazy { (context.applicationContext as KolerApp).component }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        elevation = 0f
        isAllCaps = false
        typeface = ResourcesCompat.getFont(context, R.font.google_sans_bold)
        background = componentRoot.drawableInteractor.getDrawable(R.drawable.bubble_background)
        backgroundTintList = backgroundTintList ?: ColorStateList.valueOf(
            componentRoot.colorInteractor.getAttrColor(R.attr.colorSecondary)
        )
        setTextColor(ColorStateList.valueOf(componentRoot.colorInteractor.getAttrColor(R.attr.colorOnSecondary)))
    }
}