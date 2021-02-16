package com.chooloo.www.koler.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.MainPagerAdapter
import com.chooloo.www.koler.databinding.ActivityMainBinding
import com.chooloo.www.koler.ui.about.AboutActivity
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.dialpad.DialpadBottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadBottomFragment.Companion.newInstance
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.ui.menu.MenuBottomFragment
import com.chooloo.www.koler.ui.search.SearchFragment
import com.chooloo.www.koler.ui.settings.SettingsActivity
import com.chooloo.www.koler.util.requestDefaultDialer
import com.chooloo.www.koler.viewmodel.SearchViewModel

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainMvpView {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var _searchFragment: SearchFragment
    private lateinit var _searchViewModel: SearchViewModel
    private lateinit var _menuBottomFragment: MenuBottomFragment
    private lateinit var _presenter: MainMvpPresenter<MainMvpView>
    private lateinit var _dialpadBottomFragment: DialpadBottomFragment

    override var dialpadNumber: String
        get() = _dialpadBottomFragment.number
        set(value) {
            _dialpadBottomFragment.number = value
        }

    override val isDialpadShown: Boolean
        get() = _dialpadBottomFragment.isShown

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

        _searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        if (intent.action == Intent.ACTION_DIAL || intent.action == Intent.ACTION_VIEW) {
            _presenter.onViewIntent(intent)
        }

        _dialpadBottomFragment = newInstance(true)

        _menuBottomFragment = MenuBottomFragment.newInstance(R.menu.main_actions).apply {
            setOnMenuItemClickListener(_presenter::onOptionsItemSelected)
        }

        _searchFragment = SearchFragment().apply {
            setOnTextChangedListener(_presenter::onSearchTextChanged)
            supportFragmentManager.beginTransaction().replace(R.id.main_search_fragment, this).commitNow()
        }

        _binding.apply {
            mainDialpadButton.setOnClickListener { _presenter.onDialpadFabClick() }
            appbarMain.mainMenuButton.setOnClickListener { _presenter.onMenuClick() }
            mainViewPager.apply {
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        appbarMain.tabTextContacts.isEnabled = position == 0
                        appbarMain.tabTextRecents.isEnabled = position == 1
                    }
                })
                adapter = MainPagerAdapter(this@MainActivity)
            }
        }
    }

    override fun showDialpad(isShow: Boolean) {
        _dialpadBottomFragment.apply {
            if (isShown) dismiss()
            if (isShow) show(supportFragmentManager, DialpadFragment.TAG)
        }
    }

    override fun showMenu(isShow: Boolean) {
        if (isShow && !_menuBottomFragment.isShown) {
            _menuBottomFragment.show(supportFragmentManager, "main_menu_fragment")
        } else if (!isShow && _menuBottomFragment.isShown) {
            _menuBottomFragment.dismiss()
        }
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
}