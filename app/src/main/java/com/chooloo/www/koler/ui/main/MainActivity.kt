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
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.ui.menu.MenuFragment
import com.chooloo.www.koler.ui.settings.SettingsActivity
import com.chooloo.www.koler.util.permissions.requestDefaultDialer
import com.chooloo.www.koler.viewmodel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainMvpView {

    private val _presenter by lazy { MainPresenter<MainMvpView>() }
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val _dialpadFragment by lazy { DialpadFragment.newInstance(true) }
    private val _menuFragment by lazy { MenuFragment.newInstance(R.menu.main_actions) }
    private val _searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override var dialpadNumber: String
        get() = _dialpadFragment.number
        set(value) {
            _dialpadFragment.number = value
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

        _menuFragment.setOnMenuItemClickListener(_presenter::onOptionsItemSelected)

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

    override fun openDialpad() {
        BottomFragment.newInstance(_dialpadFragment)
            .show(supportFragmentManager, DialpadFragment.TAG)
    }

    override fun openMenu() {
        BottomFragment.newInstance(_menuFragment).show(supportFragmentManager, MenuFragment.TAG)
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