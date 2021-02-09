package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.WidgetIconChipBinding


@SuppressLint("Recycle", "CustomViewStyleable")
class IconButton : ConstraintLayout {
    private var _binding: WidgetIconChipBinding
    private var _defaultText: String? = null
    private var _onCLickText: String? = null
    @DrawableRes private var _defaultIcon: Int? = null
    @DrawableRes private var _onClickIcon: Int? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0) : super(context, attrs, defStyleRes) {
        _binding = WidgetIconChipBinding.inflate(LayoutInflater.from(context), this, true).apply {
            iconChipText.isClickable = false
            iconChipIcon.isClickable = false
            val padding = resources.getDimension(R.dimen.icon_chip_padding).toInt()
            root.setPadding(padding, padding, padding, padding)
        }

        context.obtainStyledAttributes(attrs, R.styleable.Koler_IconButton, defStyleRes, 0).also {
            setText(it.getString(R.styleable.Koler_IconButton_text))
            setTextOnClick(it.getString(R.styleable.Koler_IconButton_activatedText))
            setIcon(it.getResourceId(R.styleable.Koler_IconButton_icon, NO_ID))
            setIconOnClick(it.getResourceId(R.styleable.Koler_IconButton_activatedIcon, NO_ID))
        }

        setOnClickListener { isActivated = !isActivated }

        isFocusable = true
        isClickable = true
        isActivated = false
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)
        applyIcon(if (activated) _onClickIcon else _defaultIcon)
        applyText(if (activated) _onCLickText else _defaultText)
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
}