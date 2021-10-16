package com.chooloo.www.koler.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.chooloo.www.koler.R

@SuppressLint("CustomViewStyleable")
class Tabs : LinearLayout {
    private var _customTextSize: Int? = null
    private var _viewPager: ViewPager2? = null
    private var _tabs: ArrayList<Tab> = arrayListOf()

    private val _spacingSmall by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_small) }

    var viewPager: ViewPager2?
        get() = _viewPager
        set(value) {
            _viewPager = value
            _viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    selectTab(position)
                }
            })
        }

    var headers: Array<String>
        get() = _tabs.map { it.text.toString() }.toTypedArray()
        set(value) {
            replaceTabs(value.map { getTab(it) }.toTypedArray())
        }

    var textSize: Float
        get() = _tabs.getOrNull(0)?.textSize ?: 0f
        set(value) {
            _tabs.forEach { it.textSize = value }
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        orientation = HORIZONTAL

        context.obtainStyledAttributes(attrs, R.styleable.Koler_Tabs, defStyleRes, 0).also {
            val obtainedSize = it.getDimensionPixelSize(R.styleable.Koler_Tabs_textSize, 0)
            _customTextSize = if (obtainedSize == 0) null else obtainedSize
        }.recycle()
    }

    private fun addTab(tab: Tab) {
        addView(tab)
        _tabs.add(tab)
        tab.setOnClickListener { _viewPager?.currentItem = _tabs.indexOf(tab) }
    }

    private fun replaceTabs(tabs: Array<Tab>) {
        _tabs.forEach { removeView(it) }
        _tabs = arrayListOf()
        tabs.forEach { tab -> addTab(tab) }
    }

    private fun getTab(headerText: String) = Tab(context).apply {
        text = headerText
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            marginEnd = _spacingSmall + 10
        }
    }

    private fun selectTab(position: Int) {
        _tabs.forEachIndexed { index, tab -> tab.isActivated = index == position }
    }
}