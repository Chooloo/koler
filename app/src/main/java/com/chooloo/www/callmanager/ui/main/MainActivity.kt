package com.chooloo.www.callmanager.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.R
import com.chooloo.www.callmanager.adapter.MainPagerAdapter
import com.chooloo.www.callmanager.databinding.ActivityMainBinding
import com.chooloo.www.callmanager.ui.about.AboutActivity
import com.chooloo.www.callmanager.ui.base.BaseActivity
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment
import com.chooloo.www.callmanager.ui.dialpad.DialpadBottomDialogFragment.Companion.newInstance
import com.chooloo.www.callmanager.ui.dialpad.DialpadFragment
import com.chooloo.www.callmanager.ui.menu.MenuFragment
import com.chooloo.www.callmanager.ui.menu.MenuFragment.Companion.newInstance
import com.chooloo.www.callmanager.ui.search.SearchFragment
import com.chooloo.www.callmanager.ui.settings.SettingsActivity
import com.chooloo.www.callmanager.util.requestDefaultDialer
import com.chooloo.www.callmanager.viewmodel.dial.DialViewModel

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainMvpView {
    private lateinit var _presenter: MainMvpPresenter<MainMvpView>
    private lateinit var _dialViewModel: DialViewModel
    private lateinit var _binding: ActivityMainBinding
    private lateinit var _bottomDialpadFragment: DialpadBottomDialogFragment
    private lateinit var _menuFragment: MenuFragment
    private lateinit var _searchFragment: SearchFragment

    override var dialpadNumber: String
        get() = _bottomDialpadFragment.number
        set(value) {
            _bottomDialpadFragment.number = value
        }

    override val isDialpadShown: Boolean
        get() = _bottomDialpadFragment.isShown

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun onBackPressed() {
        if (!_presenter.onBackPressed()) {
            super.onBackPressed()
        }
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
        _presenter = MainPresenter()
        _presenter.attach(this)

        requestDefaultDialer()

        _bottomDialpadFragment = newInstance(true)

        _menuFragment = newInstance(R.menu.main_actions).apply {
            setOnMenuItemClickListener { menuItem: MenuItem -> _presenter.onOptionsItemSelected(menuItem) }
        }

        _searchFragment = SearchFragment().apply {
            setOnFocusChangedListener { isFocused: Boolean -> _presenter.onSearchFocusChanged(isFocused) }
            setOnTextChangedListener { text: String -> _presenter.onSearchTextChanged(text) }
        }

        _dialViewModel = ViewModelProvider(this).get(DialViewModel::class.java).apply {
            number.observe(this@MainActivity, Observer { number: String? ->
                _presenter.onDialNumberChanged(number ?: "")
            })
        }

        if (intent.action == Intent.ACTION_DIAL || intent.action == Intent.ACTION_VIEW) {
            _presenter.onViewIntent(intent)
        }

        supportFragmentManager.beginTransaction().replace(R.id.main_search_fragment, _searchFragment).commitNow()

        _binding.apply {
            dialpadFabButton.setOnClickListener { _presenter.onDialpadFabClick() }
            appbarMain.mainMenuButton.setOnClickListener { _presenter.onMenuClick() }
            appbarMain.mainTabLayout.setViewPager(_binding.viewPager)
            viewPager.adapter = MainPagerAdapter(this@MainActivity)
        }
    }

    override fun showDialpad(isShow: Boolean) {
        if (isShow) {
            _bottomDialpadFragment.dismiss()
            _bottomDialpadFragment.show(supportFragmentManager, DialpadFragment.TAG)
        } else if (_bottomDialpadFragment.isShown) {
            _bottomDialpadFragment.dismiss()
        }
    }

    override fun showMenu(isShow: Boolean) {
        if (isShow && !_menuFragment.isShown) {
            _menuFragment.show(supportFragmentManager, "main_menu_fragment")
        } else if (!isShow && _menuFragment.isShown) {
            _menuFragment.dismiss()
        }
    }

    override fun goToSettings() {
        this.startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun goToAbout() {
        this.startActivity(Intent(this, AboutActivity::class.java))
    }
}