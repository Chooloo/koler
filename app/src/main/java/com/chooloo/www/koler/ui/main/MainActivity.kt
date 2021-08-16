package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.MainPagerAdapter
import com.chooloo.www.koler.databinding.MainBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.dialpad.DialpadFragment
import com.chooloo.www.koler.ui.settings.SettingsFragment
import com.chooloo.www.koler.viewmodel.SearchViewModel

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainContract.View {
    private val _binding by lazy { MainBinding.inflate(layoutInflater) }
    private val _presenter by lazy { MainPresenter<MainContract.View>(this) }
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

    override fun onSetup() {
        _binding.apply {
            mainDialpadButton.setOnClickListener { _presenter.onDialpadFabClick() }
            mainViewPager.adapter = MainPagerAdapter(this@MainActivity)
            appbarMain.mainMenuButton.setOnClickListener { _presenter.onMenuClick() }
            appbarMain.mainTabs.headers =
                arrayOf(getText(R.string.contacts).toString(), getText(R.string.recents).toString())
            appbarMain.mainTabs.viewPager = mainViewPager
            mainViewPager.currentItem = boundComponent.preferencesInteractor.defaultPage.index
        }

        boundComponent.permissionInteractor.checkDefaultDialer()
        checkIntent()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        boundComponent.screenInteractor.ignoreEditTextFocus(event)
        return super.dispatchTouchEvent(event)
    }


    //region main view

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

    //endregion
}