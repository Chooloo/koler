package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import com.chooloo.www.koler.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.SIZE_AUTO


@SuppressLint("Recycle", "CustomViewStyleable")
class IconButton : LinearLayout {
    private var _iconFab: FloatingActionButton
    private var _textTextView: AppCompatTextView

    private var _defaultText: String? = null
    private var _onCLickText: String? = null
    @DrawableRes private var _defaultIcon: Int? = null
    @DrawableRes private var _onClickIcon: Int? = null
    @FloatingActionButton.Size private var _size: Int = SIZE_AUTO

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0) : super(context, attrs, defStyleRes) {
        context.obtainStyledAttributes(attrs, R.styleable.Koler_IconButton, defStyleRes, 0).also {
            _defaultText = it.getString(R.styleable.Koler_IconButton_text)
            _onCLickText = it.getString(R.styleable.Koler_IconButton_activatedText)
            _defaultIcon = it.getResourceId(R.styleable.Koler_IconButton_icon, NO_ID)
            _onClickIcon = it.getResourceId(R.styleable.Koler_IconButton_activatedIcon, NO_ID)
            _size = it.getInt(R.styleable.Koler_IconButton_size, SIZE_AUTO)
        }

        _iconFab = FloatingActionButton(context, attrs, defStyleRes).apply {
            size = _size
            isClickable = false
            compatElevation = 0f
            elevation = 0f
            imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_accent))
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_accent_light))

            setImageDrawable(_defaultIcon?.let { getDrawable(context, it) })
            setOnClickListener {
                isActivated = !isActivated
                _onClickIcon?.let { applyIcon(if (isActivated) it else _defaultIcon) }
                _onCLickText?.let { applyText(if (isActivated) it else _defaultText) }
            }
        }.also {
            addView(it)
        }

        _textTextView = AppCompatTextView(context, attrs, defStyleRes).apply {
            text = _defaultText
            isClickable = false
            visibility = if (_defaultText != null) VISIBLE else GONE
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            setTextAppearance(R.style.Koler_Text_Caption)
        }.also {
            addView(it)
        }

        orientation = VERTICAL
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    private fun applyIcon(@DrawableRes icon: Int?) {
        _iconFab.setImageDrawable(icon?.let { getDrawable(context, it) })
    }

    private fun applyText(text: String?) {
        _textTextView.text = text
    }
}