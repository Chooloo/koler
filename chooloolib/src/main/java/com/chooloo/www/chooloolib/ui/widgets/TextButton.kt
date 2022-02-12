package com.chooloo.www.chooloolib.ui.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.util.getAttrColor

class TextButton : AppCompatButton {
    private var _textDefault: String?
    private var _textActivated: String?
    private var _foregroundTintList: ColorStateList?
    private var _backgroundTintList: ColorStateList?


    private val colorSecondary by lazy { context.getAttrColor(R.attr.colorSecondary) }
    private val colorOnSecondary by lazy { context.getAttrColor(R.attr.colorOnSecondary) }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Chooloo_TextButton, defStyleRes, 0).also {
            _textActivated = it.getString(R.styleable.Chooloo_TextButton_activatedText)
        }.recycle()

        elevation = 0f
        isAllCaps = false
        stateListAnimator = null
        gravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        typeface = ResourcesCompat.getFont(context, R.font.google_sans_bold)
        background = ContextCompat.getDrawable(context, R.drawable.bubble_background)

        backgroundTintList = backgroundTintList ?: ColorStateList.valueOf(colorSecondary)
        setTextColor(
            if (textColors.changingConfigurations == 0) textColors else ColorStateList.valueOf(
                colorOnSecondary
            )
        )

        _textDefault = text.toString()
        _foregroundTintList = textColors
        _backgroundTintList = backgroundTintList
    }


    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        refreshResources()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (isEnabled) 1f else 0.2f
    }

    private fun refreshResources() {
        setTextColor(if (isActivated) _backgroundTintList else _foregroundTintList)
        backgroundTintList = if (isActivated) _foregroundTintList else _backgroundTintList
        text = if (isActivated) _textActivated else _textDefault
    }
}