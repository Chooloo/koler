package com.chooloo.www.chooloolib.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.chooloo.www.chooloolib.R

class BannerTextView : AppCompatTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleRes
    ) {
        textAlignment = TEXT_ALIGNMENT_VIEW_START
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        background = ContextCompat.getDrawable(context, R.drawable.notification_banner_background)
        background.alpha = 50

        setTextAppearance(R.style.Chooloo_Text_Headline4)
        val padding = resources.getDimensionPixelSize(R.dimen.default_spacing_small) + 10
        setPadding(padding, padding + 20, padding, padding)
    }
}