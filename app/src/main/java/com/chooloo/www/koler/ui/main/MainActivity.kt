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
import com.chooloo.www.koler.viewmodel.DialpadViewModel

// TODO implement FAB Coordination
class MainActivity : BaseActivity(), MainContract.View {
    private var _dialpadFragment: DialpadFragment? = null
    private lateinit var _presenter: MainPresenter<MainActivity>
    private val _binding by lazy { MainBinding.inflate(layoutInflater) }
    private val _dialpadViewModel by lazy {
        ViewModelProvider(activityComponent.viewModelStoreOwner).get(DialpadViewModel::class.java)
    }

    override var selectedPage: Int
        get() = _binding.mainViewPager.currentItem
        set(value) {
            _binding.mainViewPager.currentItem = value
        }

    override var dialpadNumber: String?
        get() = _dialpadViewModel.number.value.toString()
        set(value) {
            _dialpadViewModel.number.value = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        _presenter = MainPresenter(this)
        _binding.apply {
            mainDialpadButton.setOnClickListener { _presenter.onDialpadFabClick() }
            mainViewPager.adapter = MainPagerAdapter(this@MainActivity)
            mainMenuButton.setOnClickListener { _presenter.onMenuClick() }
            mainTabs.headers =
                arrayOf(getText(R.string.contacts).toString(), getText(R.string.recents).toString())
            mainTabs.viewPager = mainViewPager
            mainViewPager.currentItem = activityComponent.preferencesInteractor.defaultPage.index
        }

        activityComponent.permissionInteractor.checkDefaultDialer()
        checkIntent()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        activityComponent.screenInteractor.ignoreEditTextFocus(event)
        return super.dispatchTouchEvent(event)
    }


    //region main view

    override fun openDialpad() {
        _dialpadFragment = DialpadFragment.newInstance(true)
        BottomFragment(_dialpadFragment!!).show(supportFragmentManager, DialpadFragment.TAG)
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
}