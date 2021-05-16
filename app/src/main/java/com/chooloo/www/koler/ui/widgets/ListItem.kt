package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.AnimationManager
import com.chooloo.www.koler.util.getSelectableItemBackgroundDrawable
import com.chooloo.www.koler.util.sizeInDp
import com.github.abdularis.civ.AvatarImageView
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_IMAGE
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_INITIAL

@SuppressLint("CustomViewStyleable", "Recycle")
open class ListItem : LinearLayout {
    private var _isCompact: Boolean = false
    private val _animationManager by lazy { AnimationManager(context) }

    private var _onLeftButtonClickListener: () -> Unit = {}
    private var _onRightButtonClickListener: () -> Unit = {}

    private val _buttonLeft: IconButton
    private val _buttonRight: IconButton
    private val _image: AvatarImageView
    private val _title: AppCompatTextView
    private val _header: AppCompatTextView
    private val _caption: AppCompatTextView
    private val _personLayout: ConstraintLayout

    private val dimenSpacing by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing) }
    private val dimenImageSize by lazy { resources.getDimensionPixelSize(R.dimen.image_size_small) }
    private val dimenSpacingSmall by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_small) }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        orientation = VERTICAL
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        _header = AppCompatTextView(context, attrs, defStyleRes).apply {
            isClickable = true
            isFocusable = true
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            typeface = ResourcesCompat.getFont(context, R.font.google_sans_bold)
            layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(5, dimenSpacing, 0, 12)
            }

            setTextAppearance(R.style.Koler_Text_Caption)
        }

        _title = AppCompatTextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(dimenSpacingSmall + 20, 0, dimenSpacing, 0)
            }

            setTextAppearance(R.style.Koler_Text_Headline4)
        }

        _caption = AppCompatTextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(0, context.sizeInDp(2), 0, 0)
            }

            setTextAppearance(R.style.Koler_Text_Caption)
        }

        _image = AvatarImageView(context, attrs).apply {
            state = SHOW_INITIAL
            id = generateViewId()
            textSize = resources.getDimension(R.dimen.caption_1)
            layoutParams = ConstraintLayout.LayoutParams(dimenImageSize, dimenImageSize)
            textColor = ContextCompat.getColor(context, R.color.color_image_placeholder_foreground)
            avatarBackgroundColor =
                ContextCompat.getColor(context, R.color.color_image_placeholder_background)
        }

        _buttonLeft = IconButton(context, attrs, defStyleRes).apply {
            visibility = GONE
            id = generateViewId()
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall
                )
            }

            setOnClickListener { _onLeftButtonClickListener.invoke() }
        }

        _buttonRight = IconButton(context, attrs, defStyleRes).apply {
            visibility = GONE
            id = generateViewId()
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall
                )
            }

            setOnClickListener { _onRightButtonClickListener.invoke() }
        }

        _personLayout = ConstraintLayout(context, attrs, defStyleRes).apply {
            isClickable = true
            id = View.generateViewId()
            background = context.getSelectableItemBackgroundDrawable()
            layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            setPadding(dimenSpacingSmall)
            addView(_image)
            addView(_title)
            addView(_caption)
            addView(_buttonLeft)
            addView(_buttonRight)
        }

        ConstraintSet().apply {
            clone(_personLayout)

            _image.id.also {
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
                connect(it, START, PARENT_ID, START)
                connect(it, TOP, PARENT_ID, TOP)
            }

            _title.id.also {
                connect(it, BOTTOM, _caption.id, TOP)
                connect(it, START, _image.id, END)
                connect(it, END, PARENT_ID, END)
                connect(it, TOP, PARENT_ID, TOP)
                setHorizontalBias(it, 0F)
            }

            _caption.id.also {
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
                connect(it, START, _title.id, START)
                connect(it, TOP, _title.id, BOTTOM)
            }

            _buttonRight.id.also {
                connect(it, END, PARENT_ID, END)
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
            }

            _buttonLeft.id.also {
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
                connect(it, END, _buttonRight.id, START)
            }

            createVerticalChain(
                PARENT_ID,
                TOP,
                PARENT_ID,
                BOTTOM,
                intArrayOf(_title.id, _caption.id),
                null,
                CHAIN_PACKED
            )

            applyTo(_personLayout)
        }

        addView(_header)
        addView(_personLayout)

        context.obtainStyledAttributes(attrs, R.styleable.Koler_ListItem, 0, 0).also {
            titleText = it.getString(R.styleable.Koler_ListItem_title)
            headerText = it.getString(R.styleable.Koler_ListItem_header)
            captionText = it.getString(R.styleable.Koler_ListItem_caption)
            imageDrawable = it.getDrawable(R.styleable.Koler_ListItem_src)
            isCompact = it.getBoolean(R.styleable.Koler_ListItem_compact, false)
        }
    }

    var isCompact: Boolean
        get() = _isCompact
        set(value) {
            if (value) {
                _personLayout.setPadding(dimenSpacing, 3, dimenSpacing, 3)
                _header.setPadding(dimenSpacing, dimenSpacingSmall, dimenSpacing, 3)
            } else {
                _personLayout.setPadding(
                    dimenSpacing,
                    dimenSpacingSmall,
                    dimenSpacing,
                    dimenSpacingSmall
                )
                _header.setPadding(
                    dimenSpacing,
                    dimenSpacingSmall,
                    context.sizeInDp(5),
                    dimenSpacingSmall
                )
            }
        }

    //region header

    var headerText: String?
        get() = _header.text.toString()
        set(value) {
            _header.apply {
                text = value
                visibility = if (value != null && value != "") VISIBLE else GONE
            }
        }

    //endregion

    //region image

    var imageSize: Int
        get() = _image.height
        set(value) {
            _image.layoutParams = LayoutParams(value, value)
        }

    var imageTextSize: Float
        get() = _image.textSize
        set(value) {
            _image.textSize = value
        }

    var imageVisibility: Boolean
        get() = _image.visibility == VISIBLE
        set(value) {
            _image.visibility = if (value) VISIBLE else GONE
        }

    var imageDrawable: Drawable?
        get() = _image.drawable
        set(value) {
            _image.setImageDrawable(value)
            _image.state = SHOW_IMAGE
        }

    fun setImageInitials(text: String?) {
        _image.text = text
        text?.let { _image.state = SHOW_INITIAL }
    }

    fun setImageUri(imageUri: Uri?) {
        _image.setImageURI(imageUri)
        _image.state = if (imageUri != null) SHOW_IMAGE else SHOW_INITIAL
    }

    fun setImageBackgroundColor(@ColorInt color: Int) {
        _image.setBackgroundColor(color)
    }

    //endregion

    //region title

    var titleText: String?
        get() = _title.text.toString()
        set(value) {
            _title.text = value ?: ""
        }

    fun setTitleTextColor(@ColorInt color: Int) {
        _title.setTextColor(color)
    }

    //endregion

    //region caption

    var captionText: String?
        get() = _caption.text.toString()
        set(value) {
            _caption.apply {
                text = value ?: ""
                visibility = if (value == null) GONE else VISIBLE
            }
        }

    fun setCaptionTextColor(@ColorInt color: Int) {
        _caption.setTextColor(color)
    }

    //endregion

    //region left button

    var leftButtonVisibility: Boolean
        get() = _buttonLeft.visibility == VISIBLE
        set(value) {
            _buttonLeft.visibility = if (value) VISIBLE else GONE
        }

    fun setLeftButtonTintColor(@ColorRes colorRes: Int) {
        _buttonLeft.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    }

    fun setLeftButtonBackgroundTintColor(@ColorInt color: Int) {
        _buttonLeft.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun setLeftButtonDrawable(@DrawableRes drawableRes: Int) {
        _buttonLeft.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
    }

    fun setOnLeftButtonClickListener(onLeftButtonClickListener: () -> Unit) {
        _onLeftButtonClickListener = onLeftButtonClickListener
    }

    //endregion

    //region right button

    var rightButtonVisibility: Boolean
        get() = _buttonRight.visibility == VISIBLE
        set(value) {
            _buttonRight.visibility = if (value) VISIBLE else GONE
        }

    fun setRightButtonTintColor(@ColorRes colorRes: Int) {
        _buttonRight.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    }

    fun setRightButtonBackgroundTintColor(@ColorInt color: Int) {
        _buttonRight.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun setRightButtonDrawable(@DrawableRes drawableRes: Int) {
        _buttonRight.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
    }

    fun setOnRightButtonClickListener(onRightButtonClickListener: () -> Unit) {
        _onRightButtonClickListener = onRightButtonClickListener
    }

    //endregion

    fun blinkCaption() {
        _animationManager.blinkView(_caption, 2500)
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        _personLayout.setOnClickListener(onClickListener)
    }

    override fun setOnLongClickListener(onLongClickListener: OnLongClickListener?) {
        _personLayout.setOnLongClickListener(onLongClickListener)
    }
}