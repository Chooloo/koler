package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.MainPagerAdapter
import com.chooloo.www.koler.databinding.ActivityMainBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.ui.settings.SettingsFragment
import com.chooloo.www.koler.util.ignoreEditTextFocus
import com.chooloo.www.koler.util.permissions.checkDefaultDialer
import com.chooloo.www.koler.util.preferences.KolerPreferences
import com.chooloo.www.koler.viewmodel.SearchViewModel

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainContract.View {
    private val _presenter by lazy { MainPresenter<MainContract.View>() }
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val _searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override var selectedPage: Int
        get() = _binding.mainViewPager.currentItem
        set(value) {
            _binding.mainViewPager.currentItem = value
        }

    override var dialpadNumber: String
        get() = _searchViewModel.number.value.toString()
        set(value) {
            _searchViewModel.number.value = value
        }

    override var liveContactsText: String?
        get() = _searchViewModel.contactsText.value
        set(value) {
            _searchViewModel.contactsText.value = value
        }

    override var liveRecentsText: String?
        get() = _searchViewModel.recentsText.value
        set(value) {
            _searchViewModel.recentsText.value = value
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
        ignoreEditTextFocus(event)
        return super.dispatchTouchEvent(event)
    }

    override fun onSetup() {
        _presenter.attach(this)

        _binding.apply {
            mainDialpadButton.setOnClickListener { _presenter.onDialpadFabClick() }
            mainViewPager.adapter = MainPagerAdapter(this@MainActivity)
            appbarMain.mainMenuButton.setOnClickListener { _presenter.onMenuClick() }
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
            mainViewPager.currentItem = KolerPreferences(this@MainActivity).defaultPage.index
        }

        checkDefaultDialer()
        checkIntent()
    }

    override fun openDialpad() {
        BottomFragment(DialpadFragment.newInstance(true).apply {
            setOnTextChangedListener(_presenter::onDialpadTextChanged)
        }).show(supportFragmentManager, DialpadFragment.TAG)
    }

    override fun openSettings() {
        BottomFragment(SettingsFragment.newInstance()).show(
            supportFragmentManager,
            SettingsFragment.TAG
        )
    }

    override fun checkIntent() {
        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            _presenter.onViewIntent(intent)
        }
    }

    override fun updateSearchViewModelNumber(text: String?) {
        _searchViewModel.number.value = text
    }
}