package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.setPadding
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.IconChipLayoutBinding


@SuppressLint("Recycle")
class IconChip constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleRes) {

    private var _binding: IconChipLayoutBinding
    private var _defaultText: String? = null
    private var _onCLickText: String? = null
    @DrawableRes private var _defaultIcon: Int? = null
    @DrawableRes private var _onClickIcon: Int? = null
    @ColorRes private var _backgroundColor: Int
    @ColorRes private var _backgroundColorActivated: Int

    companion object {
        @ColorRes private const val DEFAULT_BACKGROUND_COLOR: Int = R.color.grey_100
        @ColorRes private const val DEFAULT_BACKGROUND_COLOR_ACTIVATED: Int = R.color.grey_400
    }

    init {
        _backgroundColor = DEFAULT_BACKGROUND_COLOR
        _backgroundColorActivated = DEFAULT_BACKGROUND_COLOR_ACTIVATED

        context.obtainStyledAttributes(attrs, R.styleable.Koler_IconChip, defStyleRes, 0).also {
            setText(it.getString(R.styleable.Koler_IconChip_text))
            setTextOnClick(it.getString(R.styleable.Koler_IconChip_activatedText))
            setIcon(it.getResourceId(R.styleable.Koler_IconChip_icon, NO_ID))
            setIconOnClick(it.getResourceId(R.styleable.Koler_IconChip_activatedIcon, NO_ID))
            setDefaultBackgroundColor(it.getResourceId(R.styleable.Koler_IconChip_backgroundColor, DEFAULT_BACKGROUND_COLOR))
            setBackgroundColorActivated(it.getResourceId(R.styleable.Koler_IconChip_backgroundColorActivated, DEFAULT_BACKGROUND_COLOR_ACTIVATED))
        }

        setOnClickListener { isActivated = !isActivated }
        setPadding(resources.getDimension(R.dimen.icon_chip_padding).toInt())

        _binding = IconChipLayoutBinding.inflate(LayoutInflater.from(context), this, true).apply {
            iconChipText.isClickable = false
            iconChipIcon.isClickable = false
        }

        isFocusable = true
        isClickable = true
        isActivated = false
        background = getBackgroundDrawable()
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        applyIcon(if (activated) _onClickIcon else _defaultIcon)
        applyText(if (activated) _onCLickText else _defaultText)
        background = getBackgroundDrawable()
    }

    private fun getBackgroundDrawable(): Drawable {
        val typeValue = TypedValue().also {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true)
        }
        return (getDrawable(context, typeValue.resourceId) as GradientDrawable).apply {
            cornerRadius = 200f
            setColor(getColor(context, if (isActivated) _backgroundColorActivated else _backgroundColor))
        }
    }

    private fun applyIcon(@DrawableRes icon: Int?) {
        _binding.iconChipIcon.setImageDrawable(icon?.let { getDrawable(context, it) })
    }

    private fun applyText(text: String?) {
        _binding.iconChipText.text = text
    }

    fun setText(text: String?) {
        _defaultText = text
        applyText(text)
    }

    fun setTextOnClick(textOnClick: String?) {
        _onCLickText = textOnClick
    }

    fun setIcon(@DrawableRes icon: Int) {
        _defaultIcon = icon
        applyIcon(_defaultIcon)
    }

    fun setIconOnClick(@DrawableRes onClickIcon: Int) {
        _onClickIcon = onClickIcon
    }

    fun setDefaultBackgroundColor(@ColorRes backgroundColor: Int) {
        _backgroundColor = backgroundColor
    }

    fun setBackgroundColorActivated(@ColorRes backgroundColorActivated: Int) {
        _backgroundColorActivated = backgroundColorActivated
    }
}