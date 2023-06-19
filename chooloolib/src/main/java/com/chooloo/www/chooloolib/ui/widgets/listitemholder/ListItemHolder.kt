package com.chooloo.www.chooloolib.ui.widgets.listitemholder

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.databinding.ListItemBinding
import com.chooloo.www.chooloolib.util.getAttrColor
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_IMAGE
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_INITIAL

open class ListItemHolder(protected val binding: ListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var _onLeftButtonClickListener: () -> Unit = {}
    private var _onRightButtonClickListener: () -> Unit = {}
    protected val context get() = itemView.context

    val dimenSpacing by lazy { context.resources.getDimensionPixelSize(R.dimen.default_spacing) }

    var isClickable: Boolean
        get() = binding.listItemMainLayout.isClickable
        set(value) {
            binding.listItemMainLayout.isClickable = value
            if (!value) {
                binding.listItemTitle.setTextColor(binding.listItemCaption.currentTextColor)
                binding.listItemMainLayout.setOnClickListener(null)
            }
        }

    var showImageOrInitials: Boolean
        get() = binding.listItemImage.state == SHOW_IMAGE
        set(value) {
            binding.listItemImage.state = if (value) SHOW_IMAGE else SHOW_INITIAL
        }

    var imageInitials: String?
        get() = binding.listItemImage.text
        set(value) {
            binding.listItemImage.text = value
            showImageOrInitials = false
        }

    var isImageVisible: Boolean
        get() = binding.listItemImage.isVisible
        set(value) {
            binding.listItemImage.isVisible = value
        }

    var titleText: String?
        get() = binding.listItemTitle.text.toString()
        set(value) {
            binding.listItemTitle.text = value
        }

    var headerText: String?
        get() = binding.listItemHeader.text.toString()
        set(value) {
            binding.listItemHeader.text = value
            binding.listItemHeader.isVisible = value != null && value.isNotEmpty()
        }

    var captionText: String?
        get() = binding.listItemCaption.text.toString()
        set(value) {
            binding.listItemCaption.text = value
            binding.listItemCaption.isVisible = value != null && value.isNotEmpty()
        }

    var isLeftButtonVisible: Boolean
        get() = binding.listItemLeftButton.isVisible
        set(value) {
            binding.listItemLeftButton.isVisible = value
        }

    var isLeftButtonEnabled: Boolean
        get() = binding.listItemLeftButton.isEnabled
        set(value) {
            binding.listItemLeftButton.isEnabled = value
        }

    var isRightButtonVisible: Boolean
        get() = binding.listItemRightButton.isVisible
        set(value) {
            binding.listItemRightButton.isVisible = value
        }

    var isRightButtonEnabled: Boolean
        get() = binding.listItemRightButton.isEnabled
        set(value) {
            binding.listItemRightButton.isEnabled = value
        }

    init {
        binding.listItemLeftButton.setOnClickListener { _onLeftButtonClickListener.invoke() }
        binding.listItemRightButton.setOnClickListener { _onRightButtonClickListener.invoke() }
    }

    fun setOnClickListener(onClickListener: View.OnClickListener?) {
        binding.listItemMainLayout.setOnClickListener(onClickListener)
    }

    fun setOnLongClickListener(onLongClickListener: View.OnLongClickListener?) {
        binding.listItemMainLayout.setOnLongClickListener(onLongClickListener)
    }

    fun setOnLeftButtonClickListener(onLeftButtonClickListener: () -> Unit) {
        _onLeftButtonClickListener = onLeftButtonClickListener
    }

    fun setOnRightButtonClickListener(onRightButtonClickListener: () -> Unit) {
        _onRightButtonClickListener = onRightButtonClickListener
    }

    fun highlightTitleText(text: String) {
        titleText?.indexOf(text, ignoreCase = true)?.let {
            if (it != -1) {
                val spannable = SpannableString(titleText)
                spannable.setSpan(
                    ForegroundColorSpan(context.getAttrColor(R.attr.colorPrimary)),
                    it,
                    it + text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.listItemTitle.setText(spannable, TextView.BufferType.SPANNABLE)
            }
        }
    }

    fun setImageUri(imageUri: Uri?) {
        showImageOrInitials = imageUri != null
        binding.listItemImage.setImageURI(imageUri)
    }

    fun setImageResource(imageRes: Int) {
        showImageOrInitials = true
        binding.listItemImage.setImageResource(imageRes)
    }

    fun setImageDrawable(drawable: Drawable) {
        binding.listItemImage.setImageDrawable(drawable)
        showImageOrInitials = true
    }

    fun setCaptionImageRes(imageRes: Int) {
        binding.listItemCaptionImage.setImageResource(imageRes)
        binding.listItemCaptionImage.isVisible = true
    }

    fun setLeftButtonIcon(iconRes: Int) {
        binding.listItemLeftButton.setImageResource(iconRes)
    }

    fun setLeftButtonIconTint(tintRes: Int) {
        binding.listItemLeftButton.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, tintRes))
    }

    fun setLeftButtonBackgroundTint(tintRes: Int) {
        binding.listItemLeftButton.backgroundTintList =
            ColorStateList.valueOf(context.getColor(tintRes))
    }

    fun setRightButtonIcon(iconRes: Int) {
        binding.listItemRightButton.setImageResource(iconRes)
    }

    fun setRightButtonIconTint(tintRes: Int) {
        binding.listItemRightButton.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, tintRes))
    }

    fun setRightButtonIconTintColor(@ColorInt color: Int) {
        binding.listItemRightButton.imageTintList = ColorStateList.valueOf(color)
    }


    fun setRightButtonBackgroundTint(tintRes: Int) {
        binding.listItemRightButton.backgroundTintList =
            ColorStateList.valueOf(context.getColor(tintRes))
    }

    fun setImageSize(size: Int) {
        binding.listItemImage.layoutParams =
            ConstraintLayout.LayoutParams(binding.listItemImage.layoutParams as ConstraintLayout.LayoutParams)
                .apply {
                    width = size
                    height = size
                }
    }
}
