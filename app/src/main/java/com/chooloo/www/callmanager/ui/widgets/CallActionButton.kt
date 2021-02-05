package com.chooloo.www.callmanager.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.databinding.CallActionButtonLayoutBinding

class CallActionButton constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleRes) {
    private var _binding: CallActionButtonLayoutBinding
    private var _defaultText: String? = null
    private var _onCLickText: String? = null
    @DrawableRes private var mDefaultIcon = 0
    @DrawableRes private var mOnClickIcon = 0

    init {
        _binding = CallActionButtonLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        context.obtainStyledAttributes(attrs, R.styleable.CallActionButton, defStyleRes, 0).also {
            setText(it.getString(R.styleable.CallActionButton_text))
            setTextOnClick(it.getString(R.styleable.CallActionButton_activatedText))
            setIcon(it.getResourceId(R.styleable.CallActionButton_icon, NO_ID))
            setIconOnClick(it.getResourceId(R.styleable.CallActionButton_activatedIcon, NO_ID))
        }

        isFocusable = true
        isClickable = true

        setOnClickListener {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
            toggleActivated()
        }
    }

    private fun setText(text: String?) {
        _defaultText = text
        applyText(text)
    }

    private fun setTextOnClick(textOnClick: String?) {
        _onCLickText = textOnClick
    }

    private fun setIconOnClick(@DrawableRes onClickIcon: Int) {
        mOnClickIcon = onClickIcon
    }

    fun setIcon(@DrawableRes icon: Int) {
        mDefaultIcon = icon
        applyIcon(icon)
    }

    private fun applyIcon(@DrawableRes icon: Int) {
        _binding.callActionIcon.setImageDrawable(ContextCompat.getDrawable(context, icon))
    }

    private fun applyText(text: String?) {
        _binding.callActionText.text = text
    }

    private fun toggleActivated() {
        isActivated = !isActivated
        if (mOnClickIcon != NO_ID) {
            applyIcon(if (isActivated) mOnClickIcon else mDefaultIcon)
        }
        if (_onCLickText != null) {
            applyText(if (isActivated) _onCLickText else _defaultText)
        }
    }
}