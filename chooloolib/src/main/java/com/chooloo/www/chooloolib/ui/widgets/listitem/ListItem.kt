package com.chooloo.www.chooloolib.ui.widgets.listitem

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.widget.ImageViewCompat
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.widgets.IconButton
import com.chooloo.www.chooloolib.ui.widgets.IconButton.Companion.SIZE_SMALL
import com.chooloo.www.chooloolib.util.getAttrColor
import com.chooloo.www.chooloolib.util.getSelectableItemBackgroundDrawable
import com.chooloo.www.chooloolib.util.getSizeInDp
import com.github.abdularis.civ.AvatarImageView
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_IMAGE
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_INITIAL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomViewStyleable", "Recycle")
open class ListItem : LinearLayout {
    private var _isPadded: Boolean = true
    private var _isCompact: Boolean = false

    private var _onLeftButtonClickListener: () -> Unit = {}
    private var _onRightButtonClickListener: () -> Unit = {}

    protected val title: TextView
    protected val header: TextView
    protected val caption: TextView
    protected val image: AvatarImageView
    protected val buttonLeft: IconButton
    protected val buttonRight: IconButton
    protected val personLayout: ConstraintLayout

    protected val dimenSpacing by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing) }
    protected val dimenImageSize by lazy { resources.getDimensionPixelSize(R.dimen.image_size_small) }
    protected val dimenSpacingBig by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_big) }
    protected val dimenSpacingSmall by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_small) }


    var imageSize: Int
        get() = image.height
        set(value) {
            image.layoutParams = ConstraintLayout.LayoutParams(value, value)
        }

    var isPadded: Boolean
        get() = _isPadded
        set(value) {
            setPaddingMode(_isCompact, value)
            _isPadded = value
        }

    var isCompact: Boolean
        get() = _isCompact
        set(value) {
            setPaddingMode(value, _isPadded)
        }

    var titleText: String?
        get() = title.text.toString()
        set(value) {
            title.text = value ?: ""
        }

    var headerText: String?
        get() = header.text.toString()
        set(value) {
            header.apply {
                text = value
                visibility = if (value != null && value != "") VISIBLE else GONE
            }
        }

    var captionText: String?
        get() = caption.text.toString()
        set(value) {
            caption.apply {
                text = value ?: ""
                visibility = if (value == null) GONE else VISIBLE
            }
        }

    var imageTextSize: Float
        get() = image.textSize
        set(value) {
            image.textSize = value
        }

    var imageTintList: ColorStateList?
        get() = image.imageTintList
        set(value) {
            ImageViewCompat.setImageTintList(image, value)
        }

    var imageVisibility: Boolean
        get() = image.isVisible
        set(value) {
            image.isVisible = value
            if (!value) {
                title.layoutParams =
                    ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        setMargins(0, 0, title.marginTop, 0)
                    }
            }
        }

    var imageDrawable: Drawable?
        get() = image.drawable
        set(value) {
            image.setImageDrawable(value)
            image.state = SHOW_IMAGE
        }

    var isLeftButtonVisible: Boolean
        get() = buttonLeft.isVisible
        set(value) {
            buttonLeft.isVisible = value
        }

    var isLeftButtonEnabled: Boolean
        get() = buttonLeft.isEnabled
        set(value) {
            buttonLeft.isEnabled = value
        }

    var isRightButtonVisible: Boolean
        get() = buttonRight.isVisible
        set(value) {
            buttonRight.isVisible = value
        }

    var isRightButtonEnabled: Boolean
        get() = buttonRight.isEnabled
        set(value) {
            buttonRight.isEnabled = value
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        clipToOutline = true
        orientation = VERTICAL
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        header = TextView(context, attrs, defStyleRes).apply {
            isClickable = true
            isFocusable = true
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(dimenSpacingSmall, dimenSpacing, dimenSpacingBig, dimenSpacingSmall)
            }

            setTextAppearance(R.style.Chooloo_Text_Subtitle2)
        }

        title = TextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(dimenSpacing - 5, 0, dimenSpacing, 0)
            }

            setTextAppearance(R.style.Chooloo_Text_Headline4)
        }

        caption = TextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

            setTextAppearance(R.style.Chooloo_Text_Caption)
            setPadding(0, context.getSizeInDp(2), 0, 0)
        }

        image = AvatarImageView(context, attrs).apply {
            state = SHOW_INITIAL
            id = generateViewId()
            textSize = resources.getDimension(R.dimen.caption_1)
            layoutParams = ConstraintLayout.LayoutParams(dimenImageSize, dimenImageSize)
            textColor = ContextCompat.getColor(context, R.color.color_image_placeholder_foreground)
            avatarBackgroundColor =
                ContextCompat.getColor(context, R.color.color_image_placeholder_background)
        }

        buttonLeft = IconButton(context, attrs, defStyleRes).apply {
            visibility = GONE
            id = generateViewId()
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
                it.setMargins(
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacing,
                    dimenSpacingSmall
                )
            }

            setSize(SIZE_SMALL)
            setOnClickListener { _onLeftButtonClickListener.invoke() }
        }

        buttonRight = IconButton(context, attrs, defStyleRes).apply {
            visibility = GONE
            id = generateViewId()
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also {
                it.setMargins(
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall
                )
            }

            setSize(SIZE_SMALL)
            setOnClickListener { _onRightButtonClickListener.invoke() }
        }

        personLayout = ConstraintLayout(context, attrs, defStyleRes).apply {
            isClickable = true
            clipToOutline = true
            id = View.generateViewId()
            foreground = context.getSelectableItemBackgroundDrawable()
            layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            background = ContextCompat.getDrawable(context, R.drawable.round_outline)
        }

        context.obtainStyledAttributes(attrs, R.styleable.Chooloo_ListItem, 0, 0).also {
            titleText = it.getString(R.styleable.Chooloo_ListItem_title)
            headerText = it.getString(R.styleable.Chooloo_ListItem_header)
            captionText = it.getString(R.styleable.Chooloo_ListItem_caption)
            imageDrawable = it.getDrawable(R.styleable.Chooloo_ListItem_src)
            isCompact = it.getBoolean(R.styleable.Chooloo_ListItem_compact, false)
        }

        personLayout.apply {
            addView(image)
            addView(title)
            addView(caption)
            addView(buttonLeft)
            addView(buttonRight)
        }

        ConstraintSet().apply {
            clone(personLayout)

            image.id.also {
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, START, PARENT_ID, START)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
            }

            title.id.also {
                setHorizontalBias(it, 0F)
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, END, PARENT_ID, END)
                connect(it, START, image.id, END)
                connect(it, BOTTOM, caption.id, TOP)
            }

            caption.id.also {
                connect(it, TOP, title.id, BOTTOM)
                connect(it, START, title.id, START)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
            }

            buttonRight.id.also {
                connect(it, END, PARENT_ID, END)
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
                setMargin(it, END, dimenSpacingSmall)
            }

            buttonLeft.id.also {
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
                connect(it, END, buttonRight.id, START)
                setMargin(it, END, dimenSpacing)
            }

            createVerticalChain(
                PARENT_ID,
                TOP,
                PARENT_ID,
                BOTTOM,
                intArrayOf(title.id, caption.id),
                null,
                CHAIN_PACKED
            )

            applyTo(personLayout)
        }

        addView(header)
        addView(personLayout)
    }

    override fun setBackground(background: Drawable?) {
        personLayout.background = background
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            personLayout.backgroundTintList =
                ColorStateList.valueOf(context.getAttrColor(R.attr.colorSecondary))
        } else {
            personLayout.background =
                ContextCompat.getDrawable(context, R.drawable.bubble_background)
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        personLayout.setOnClickListener(onClickListener)
    }

    override fun setOnLongClickListener(onLongClickListener: OnLongClickListener?) {
        personLayout.setOnLongClickListener(onLongClickListener)
    }


    protected open fun setPaddingMode(isCompact: Boolean, isEnabled: Boolean) {
        personLayout.setPadding(
            if (isEnabled) dimenSpacing else 0,
            if (isCompact) 3 else dimenSpacing - 7,
            if (isEnabled) dimenSpacing else 0,
            if (isCompact) 3 else dimenSpacing - 7
        )
        header.setPadding(
            if (isEnabled) dimenSpacing else 0,
            if (isCompact) dimenSpacingSmall - 10 else dimenSpacingSmall,
            if (isEnabled) dimenSpacing else 0,
            if (isCompact) dimenSpacingSmall else dimenSpacingSmall
        )
    }


    fun setImageUri(imageUri: Uri?) {
        image.setImageURI(imageUri)
        image.state = if (imageUri != null) SHOW_IMAGE else SHOW_INITIAL
    }

    fun setImageInitials(text: String?) {
        image.text = text
        text?.let { image.state = SHOW_INITIAL }
    }

    fun setTitleBold(isBold: Boolean) {
        title.typeface = ResourcesCompat.getFont(
            context,
            if (isBold) R.font.google_sans_medium else R.font.google_sans_regular
        )
    }


    fun setPaddingTop(top: Int) {
        personLayout.setPadding(
            personLayout.paddingLeft,
            top,
            personLayout.paddingRight,
            personLayout.paddingBottom
        )
    }

    fun setPaddingBottom(bottom: Int) {
        personLayout.setPadding(
            personLayout.paddingLeft,
            personLayout.paddingTop,
            personLayout.paddingRight,
            bottom
        )
    }

    fun highlightTitleText(text: String) {
        titleText?.indexOf(text, ignoreCase = true)?.let {
            if (it != -1) {
                val spannable = SpannableString(titleText)
                spannable.setSpan(
                    ForegroundColorSpan(context.getAttrColor(R.attr.colorOnSecondary)),
                    it,
                    it + text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                title.setText(spannable, TextView.BufferType.SPANNABLE)
            }
        }
    }

    fun setImageTint(@ColorInt color: Int) {
        imageTintList = ColorStateList.valueOf(color)
    }

    fun setTitleColor(@ColorInt color: Int) {
        title.setTextColor(color)
    }

    fun setImageResource(@DrawableRes res: Int) {
        image.setImageResource(res)
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        title.setTextAppearance(resId)
    }

    fun setImageBackgroundColor(@ColorInt color: Int) {
        image.setBackgroundColor(color)
    }

    fun setLeftButtonTintColor(@ColorRes colorRes: Int) {
        buttonLeft.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    }

    fun setLeftButtonDrawable(@DrawableRes drawableRes: Int) {
        buttonLeft.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
    }

    fun setLeftButtonBackgroundTintColor(@ColorRes colorRes: Int) {
        buttonLeft.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    }

    fun setOnLeftButtonClickListener(onLeftButtonClickListener: () -> Unit) {
        _onLeftButtonClickListener = onLeftButtonClickListener
    }

    fun setRightButtonTintColor(@ColorRes colorRes: Int) {
        buttonRight.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    }

    fun setRightButtonBackgroundTintColor(@ColorRes colorRes: Int) {
        buttonRight.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    }

    fun setRightButtonDrawable(@DrawableRes drawableRes: Int) {
        buttonRight.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
    }

    fun setOnRightButtonClickListener(onRightButtonClickListener: () -> Unit) {
        _onRightButtonClickListener = onRightButtonClickListener
    }
}