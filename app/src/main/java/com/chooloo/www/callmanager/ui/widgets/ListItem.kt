package com.chooloo.www.callmanager.ui.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.databinding.ListItemBinding

class ListItem(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleRes) {
    private var _binding: ListItemBinding

    init {
        _binding = ListItemBinding.inflate(LayoutInflater.from(context), this, true)

        context.obtainStyledAttributes(attrs, R.styleable.ListItem, 0, 0).also {
            setBigText(it.getString(R.styleable.ListItem_bigText))
            setSmallText(it.getString(R.styleable.ListItem_smallText))
            setImageDrawable(it.getDrawable(R.styleable.ListItem_src))
            setHeaderText(it.getString(R.styleable.ListItem_header))
            showHeader(_binding.listItemHeaderText.text != null)
            it.recycle()
        }

        // set clickable
        isClickable = false
        _binding.apply {
            listItemHeaderLayout.isClickable = false
            listItemHeaderLayout.isFocusable = false
            listItemPersonLayout.isClickable = true
            listItemPersonLayout.isFocusable = true
        }

        // set selectable background resource
        TypedValue().also {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true)
            setBackgroundResource(it.resourceId)
        }

        // set width and height
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setBigText(text: String?) {
        _binding.listItemBigText.text = text ?: ""
    }

    fun setSmallText(text: String?) {
        _binding.apply {
            listItemSmallText.text = text ?: ""
            listItemSmallText.visibility = if (text == null) GONE else VISIBLE
        }
    }

    fun setImageDrawable(image: Drawable?) {
        _binding.listItemImage.setImageDrawable(image)
    }

    fun setImageUri(image: Uri?) {
        _binding.listItemImage.setImageURI(image)
    }

    fun setImageBackgroundColor(@ColorInt color: Int) {
        _binding.listItemImage.setBackgroundColor(color)
    }

    fun setHeaderText(headerText: String?) {
        _binding.listItemHeaderText.text = headerText
    }

    fun showHeader(isShow: Boolean) {
        _binding.listItemHeaderLayout.visibility = if (isShow) VISIBLE else GONE
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        _binding.listItemPersonLayout.setOnClickListener(onClickListener)
    }

    override fun setOnLongClickListener(onLongClickListener: OnLongClickListener?) {
        _binding.listItemPersonLayout.setOnLongClickListener(onLongClickListener)
    }
}