package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.ListItemBinding

@SuppressLint("CustomViewStyleable", "Recycle")
class ListItem : LinearLayout {

    private var _binding: ListItemBinding

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0) : super(context, attrs, defStyleRes) {
        _binding = ListItemBinding.inflate(LayoutInflater.from(context), this, true).apply {
            root.apply {
                isClickable = false
                layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
            listItemHeaderLayout.apply {
                isClickable = false
                isFocusable = false
            }
            listItemPersonLayout.apply {
                isClickable = true
                isFocusable = true
            }
        }

        context.obtainStyledAttributes(attrs, R.styleable.Koler_ListItem, 0, 0).also {
            setBigText(it.getString(R.styleable.Koler_ListItem_bigText))
            setSmallText(it.getString(R.styleable.Koler_ListItem_smallText))
            setImageDrawable(it.getDrawable(R.styleable.Koler_ListItem_src))
            setHeaderText(it.getString(R.styleable.Koler_ListItem_header))
            showHeader(_binding.listItemHeaderText.text != null)
        }
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