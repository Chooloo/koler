package com.chooloo.www.koler.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.MainPagerAdapter
import com.chooloo.www.koler.databinding.ActivityMainBinding
import com.chooloo.www.koler.ui.about.AboutActivity
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.ui.menu.MenuFragment
import com.chooloo.www.koler.ui.settings.SettingsActivity
import com.chooloo.www.koler.util.permissions.checkDefaultDialer
import com.chooloo.www.koler.viewmodel.SearchViewModel

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainContract.View {
    private val _presenter by lazy { MainPresenter<MainContract.View>() }
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val _bottomDialpadFragment by lazy { BottomFragment(DialpadFragment.newInstance(true)) }
    private val _searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    private val _bottomMenuFragment by lazy { BottomFragment(MenuFragment.newInstance(R.menu.main_actions)) }

    override var dialpadNumber: String
        get() = _bottomDialpadFragment.fragment.number
        set(value) {
            _bottomDialpadFragment.fragment.number = value
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

        _bottomMenuFragment.fragment.setOnMenuItemClickListener(_presenter::onOptionsItemSelected)

        _binding.apply {
            mainDialpadButton.setOnClickListener { _presenter.onDialpadFabClick() }
            mainViewPager.adapter = MainPagerAdapter(this@MainActivity)
            appbarMain.apply {
                mainMenuButton.setOnClickListener { _presenter.onMenuClick() }
                mainSearchBar.setOnTextChangedListener(_presenter::onSearchTextChanged)
            }
            mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> appbarMain.apply {
                            mainTabHeader.text = getText(R.string.contacts)
                            mainTabLeftArrow.visibility = GONE
                            mainTabRightArrow.visibility = VISIBLE
                        }
                        1 -> appbarMain.apply {
                            mainTabHeader.text = getText(R.string.recents)
                            mainTabLeftArrow.visibility = VISIBLE
                            mainTabRightArrow.visibility = GONE
                        }
                    }
                }
            })
        }

        checkDefaultDialer()
        checkIntent()
    }

    override fun openDialpad() {
        _bottomDialpadFragment.show(supportFragmentManager, DialpadFragment.TAG)
    }

    override fun openMenu() {
        _bottomMenuFragment.show(supportFragmentManager, MenuFragment.TAG)
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