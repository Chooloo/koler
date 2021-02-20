package com.chooloo.www.koler.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.MainPagerAdapter
import com.chooloo.www.koler.databinding.ActivityMainBinding
import com.chooloo.www.koler.ui.about.AboutActivity
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.dialpad.DialpadBottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.ui.menu.MenuBottomFragment
import com.chooloo.www.koler.ui.settings.SettingsActivity
import com.chooloo.www.koler.util.permissions.requestDefaultDialer
import com.chooloo.www.koler.viewmodel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainMvpView {

    private val _presenter by lazy { MainPresenter<MainMvpView>() }
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val _dialpadBottomFragment by lazy { DialpadBottomFragment.newInstance(true) }
    private val _menuBottomFragment by lazy { MenuBottomFragment.newInstance(R.menu.main_actions) }
    private val _searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override var dialpadNumber: String
        get() = _dialpadBottomFragment.number
        set(value) {
            _dialpadBottomFragment.number = value
        }

    override var isDialpadVisible: Boolean
        get() = _dialpadBottomFragment.isShown
        set(value) {
            _dialpadBottomFragment.apply {
                if (value) show(supportFragmentManager, DialpadFragment.TAG) else dismiss()
            }
        }

    override var isMenuVisible: Boolean
        get() = _menuBottomFragment.isShown
        set(value) {
            _menuBottomFragment.apply {
                if (value) show(supportFragmentManager, "main_menu_fragment") else dismiss()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onSetup() {
        _presenter.attach(this)

        _menuBottomFragment.setOnMenuItemClickListener(_presenter::onOptionsItemSelected)

        _binding.apply {
            mainDialpadButton.setOnClickListener { _presenter.onDialpadFabClick() }
            mainViewPager.adapter = MainPagerAdapter(this@MainActivity)
            appbarMain.apply {
                mainMenuButton.setOnClickListener { _presenter.onMenuClick() }
                mainSearchBar.setOnTextChangedListener(_presenter::onSearchTextChanged)
            }
            TabLayoutMediator(appbarMain.mainTabLayout, mainViewPager) { tab, position ->
                tab.text = getString(if (position == 0) R.string.contacts else R.string.recents)
            }.attach()
        }

        requestDefaultDialer()
        checkIntent()
    }

    override fun goToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun goToAbout() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    override fun updateSearchViewModelText(text: String?) {
        _searchViewModel.text.value = text
    }

    override fun checkIntent() {
        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            _presenter.onViewIntent(intent)
        }
    }
}