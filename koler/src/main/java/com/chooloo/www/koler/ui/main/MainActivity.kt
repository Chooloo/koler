package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MotionEvent
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chooloo.www.chooloolib.interactor.screen.ScreensInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.MainBinding
import com.chooloo.www.koler.di.factory.controller.ControllerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), MainContract.View {
    override val contentView by lazy { binding.root }
    override val controller by lazy { kolerControllerFactory.getMainController(this) }

    override var searchText: String?
        get() = binding.mainSearchBar.text
        set(value) {
            binding.mainSearchBar.text = value
        }

    override var currentPageIndex: Int
        get() = binding.mainViewPager.currentItem
        set(value) {
            binding.mainViewPager.currentItem = value
        }

    override var headers: Array<String>
        get() = binding.mainTabs.headers
        set(value) {
            binding.mainTabs.headers = value
        }

    private val binding by lazy { MainBinding.inflate(layoutInflater) }

    @Inject lateinit var screensInteractor: ScreensInteractor
    @Inject lateinit var kolerControllerFactory: ControllerFactory


    override fun onSetup() {
        controller.init()
        binding.apply {
            mainMenuButton.setOnClickListener { controller.onMenuClick() }
            mainDialpadButton.setOnClickListener { controller.onDialpadFabClick() }
            mainTabs.viewPager = mainViewPager
            mainSearchBar.setOnTextChangedListener(controller::onSearchTextChange)
            mainSearchBar.editText?.setOnFocusChangeListener { _, hasFocus ->
                controller.onSearchFocusChange(hasFocus)
            }
            mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    controller.onPageChange(position)
                }
            })
        }

        checkIntent()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        screensInteractor.ignoreEditTextFocus(event)
        return super.dispatchTouchEvent(event)
    }

    override fun setFragmentsAdapter(count: Int, adapter: (position: Int) -> BaseFragment) {
        binding.mainViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = count
            override fun createFragment(position: Int) = adapter.invoke(position)
        }
    }

    override fun showSearching() {
        binding.root.transitionToState(R.id.constraint_set_main_collapsed)
    }

    override fun setSearchHint(hint: String) {
        binding.mainSearchBar.hint = hint
    }

    override fun checkIntent() {
        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            controller.onViewIntent(intent)
        }
    }
}