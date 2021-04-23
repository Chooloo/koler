package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView.ScaleType.FIT_CENTER
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.view.setPadding
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.getSelectableItemBackgroundDrawable
import com.chooloo.www.koler.util.sizeInDp
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

@SuppressLint("CustomViewStyleable", "Recycle")
class ListItem : LinearLayout {
    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing) }
    private val spacingSmall by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_small) }
    private val spacingBig by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_big) }
    private val imageSize by lazy { resources.getDimensionPixelSize(R.dimen.image_size_small) }

    private var title: AppCompatTextView
    private var image: ShapeableImageView
    private var header: AppCompatTextView
    private var caption: AppCompatTextView
    private var personLayout: ConstraintLayout
    private var _isCompact: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = VERTICAL

        header = AppCompatTextView(context, attrs, defStyleRes).apply {
            isClickable = true
            isFocusable = true
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            setTextAppearance(R.style.Koler_Text_Caption)
        }

        title = AppCompatTextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(0, 0, spacing, 0)
            }

            setTextAppearance(R.style.Koler_Text_Headline4)
        }

        caption = AppCompatTextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(0, context.sizeInDp(2), 0, 0)
            }

            setTextAppearance(R.style.Koler_Text_Caption)
        }

        image = ShapeableImageView(context, attrs, defStyleRes).apply {
            scaleType = FIT_CENTER
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(imageSize, imageSize)
            shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(50f)
        }

        personLayout = ConstraintLayout(context, attrs, defStyleRes).apply {
            isClickable = true
            id = View.generateViewId()
            background = context.getSelectableItemBackgroundDrawable()
            layoutParams = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

            setPadding(spacingSmall)
//            setPadding(spacingSmall, spacingSmall, spacingSmall, spacingSmall)
            addView(image)
            addView(title)
            addView(caption)
        }

        ConstraintSet().apply {
            clone(personLayout)

            //region image
            connect(image.id, BOTTOM, PARENT_ID, BOTTOM)
            connect(image.id, END, PARENT_ID, END)
            connect(image.id, TOP, PARENT_ID, TOP)
            //endregion

            //region title
            connect(title.id, BOTTOM, caption.id, TOP)
            connect(title.id, END, image.id, START)
            connect(title.id, START, PARENT_ID, START)
            connect(title.id, TOP, PARENT_ID, TOP)
            setHorizontalBias(title.id, 0F)
            //endregion

            //region caption
            connect(caption.id, BOTTOM, PARENT_ID, BOTTOM)
            connect(caption.id, START, title.id, START)
            connect(caption.id, TOP, title.id, BOTTOM)
            //endregion

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

        context.obtainStyledAttributes(attrs, R.styleable.Koler_ListItem, 0, 0).also {
            titleText = it.getString(R.styleable.Koler_ListItem_title)
            headerText = it.getString(R.styleable.Koler_ListItem_header)
            captionText = it.getString(R.styleable.Koler_ListItem_caption)
            imageDrawable = it.getDrawable(R.styleable.Koler_ListItem_src)
            isCompact = it.getBoolean(R.styleable.Koler_ListItem_compact, false)
        }
    }

    var titleText: String?
        get() = title.text.toString()
        set(value) {
            title.text = value ?: ""
        }

    var captionText: String?
        get() = caption.text.toString()
        set(value) {
            caption.apply {
                text = value ?: ""
                visibility = if (value == null) GONE else VISIBLE
            }
        }

    var headerText: String?
        get() = header.text.toString()
        set(value) {
            header.apply {
                text = value
                visibility = if (value != null && value != "") VISIBLE else GONE
            }
        }

    var imageDrawable: Drawable?
        get() = image.drawable
        set(value) {
            image.setImageDrawable(value)
        }

    var isCompact: Boolean
        get() = _isCompact
        set(value) {
            if (value) {
                personLayout.setPadding(spacingBig, 3, spacing, 3)
                header.setPadding(spacing, spacingSmall, spacing, 3)
            } else {
                personLayout.setPadding(spacingBig, spacingSmall, spacing, spacingSmall)
                header.setPadding(spacing, spacingSmall, context.sizeInDp(5), spacingSmall)
            }
        }


    fun setLeftPadding(padding: Int) {
        personLayout.also {
            it.setPadding(padding, it.paddingTop, it.paddingRight, it.paddingBottom)
        }
    }

    fun setImageUri(imageUri: Uri?) {
        image.setImageURI(imageUri)
    }

    fun setImageBackgroundColor(@ColorInt color: Int) {
        image.setBackgroundColor(color)
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        personLayout.setOnClickListener(onClickListener)
    }

    override fun setOnLongClickListener(onLongClickListener: OnLongClickListener?) {
        personLayout.setOnLongClickListener(onLongClickListener)
    }
}