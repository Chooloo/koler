package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import com.chooloo.www.koler.util.getAttrColor

@SuppressLint("CustomViewStyleable", "Recycle")
class ListItem : LinearLayout {

    private val _binding by lazy {
        ListItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        context.obtainStyledAttributes(attrs, R.styleable.Koler_ListItem, 0, 0).also {
            bigText = it.getString(R.styleable.Koler_ListItem_bigText)
            smallText = it.getString(R.styleable.Koler_ListItem_smallText)
            imageDrawable = it.getDrawable(R.styleable.Koler_ListItem_src)
            headerText = it.getString(R.styleable.Koler_ListItem_header)
            isHeaderVisible = _binding.listItemHeaderText.text !in arrayOf(null, "")
        }
    }

    var bigText: String?
        get() = _binding.listItemBigText.text.toString()
        set(value) {
            _binding.listItemBigText.text = value ?: ""
        }

    var smallText: String?
        get() = _binding.listItemSmallText.text.toString()
        set(value) {
            _binding.listItemSmallText.apply {
                text = value ?: ""
                visibility = if (value == null) GONE else VISIBLE
            }
        }

    var headerText: String?
        get() = _binding.listItemHeaderText.text.toString()
        set(value) {
            _binding.listItemHeaderText.text = value
            isHeaderVisible = value != null
        }

    var isHeaderVisible: Boolean
        get() = _binding.listItemHeaderText.visibility == VISIBLE
        set(value) {
            _binding.listItemHeaderText.visibility = if (value) VISIBLE else GONE
        }

    var imageDrawable: Drawable?
        get() = _binding.listItemImage.drawable
        set(value) {
            _binding.listItemImage.setImageDrawable(value)
            setImageBackgroundColor(if (value != null) Color.TRANSPARENT else context.getAttrColor(R.attr.colorSecondary))
        }

    fun setImageUri(image: Uri?) {
        _binding.listItemImage.setImageURI(image)
        setImageBackgroundColor(if (image != null) Color.TRANSPARENT else context.getAttrColor(R.attr.colorSecondary))
    }

    fun setImageBackgroundColor(@ColorInt color: Int) {
        _binding.listItemImage.setBackgroundColor(color)
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        _binding.listItemPersonLayout.setOnClickListener(onClickListener)
    }

    override fun setOnLongClickListener(onLongClickListener: OnLongClickListener?) {
        _binding.listItemPersonLayout.setOnLongClickListener(onLongClickListener)
    }
}