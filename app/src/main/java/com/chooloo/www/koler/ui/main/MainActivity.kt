package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MotionEvent
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.MainBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialer.DialerFragment
import com.chooloo.www.koler.ui.settings.SettingsFragment

class MainActivity : BaseActivity(), MainContract.View {
    private lateinit var _controller: MainController<MainActivity>
    private val binding by lazy { MainBinding.inflate(layoutInflater) }

    override val contentView by lazy { binding.root }

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


    override fun onSetup() {
        _controller = MainController(this)

        binding.apply {
            mainMenuButton.setOnClickListener { _controller.onMenuClick() }
            mainDialpadButton.setOnClickListener { _controller.onDialpadFabClick() }
            mainTabs.viewPager = mainViewPager
            mainSearchBar.setOnTextChangedListener(_controller::onSearchTextChange)
            mainSearchBar.editText?.setOnFocusChangeListener { _, hasFocus ->
                _controller.onSearchFocusChange(hasFocus)
            }
        }

        checkIntent()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        component.screens.ignoreEditTextFocus(event)
        return super.dispatchTouchEvent(event)
    }


    override fun openDialer(text: String?) {
        BottomFragment(DialerFragment.newInstance(text)).show(
            supportFragmentManager,
            DialerFragment.TAG
        )
    }

    override fun setFragmentsAdapter(count: Int, adapter: (position: Int) -> BaseFragment) {
        binding.mainViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = count
            override fun createFragment(position: Int) = adapter.invoke(position)
        }
    }

    override fun openSettings() {
        BottomFragment(SettingsFragment.newInstance()).show(
            supportFragmentManager,
            SettingsFragment.TAG
        )
    }

    override fun showSearching() {
        binding.root.transitionToState(R.id.constraint_set_main_collapsed)
    }

    override fun setSearchHint(hint: String) {
        binding.mainSearchBar.hint = hint
    }

    override fun checkIntent() {
        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            _controller.onViewIntent(intent)
        }
    }
}