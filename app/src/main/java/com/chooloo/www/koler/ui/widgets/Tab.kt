package com.chooloo.www.koler.ui.widgets

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.ViewManager
import java.util.*

class Tab : AppCompatTextView {
    private val _viewManager by lazy { ViewManager(context) }
    private val enabledColor by lazy { _viewManager.getAttrColor(R.attr.colorOnSurface) }
    private val disabledColor by lazy { _viewManager.getAttrColor(R.attr.colorTextHint) }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        textSize = 28f
        text = text.toString().capitalize(Locale.ROOT)
        typeface = ResourcesCompat.getFont(context, R.font.google_sans_bold)

        setTextColor(enabledColor)
    }


    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        setColor(if (activated) enabledColor else disabledColor)
        if (activated) {
            animateAttention()
        }
    }


    private fun animateAttention() {
        (context.applicationContext as KolerApp).componentRoot.animationInteractor.animateIn(this)
    }

    private fun setColor(@ColorInt color: Int) {
        ValueAnimator.ofObject(ArgbEvaluator(), currentTextColor, color).apply {
            addUpdateListener { setTextColor(it.animatedValue as Int) }
            start()
        }
    }
}