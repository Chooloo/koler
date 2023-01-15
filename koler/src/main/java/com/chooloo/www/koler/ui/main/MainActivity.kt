package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.interactor.screen.ScreensInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.chooloolib.ui.contacts.ContactsViewState
import com.chooloo.www.chooloolib.ui.dialer.DialerViewState
import com.chooloo.www.chooloolib.ui.recents.RecentsViewState
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.MainBinding
import com.chooloo.www.koler.di.factory.fragment.FragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory as ChoolooFragmentFactory

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: MainViewState by viewModels()

    private val _dialerViewState: DialerViewState by viewModels()
    private val recentsViewState: RecentsViewState by viewModels()
    private val contactsViewState: ContactsViewState by viewModels()
    private val binding by lazy { MainBinding.inflate(layoutInflater) }
    private val _fragments by lazy { listOf(_contactsFragment, _recentsFragment) }
    private val _recentsFragment by lazy { choolooFragmentFactory.getRecentsFragment() }
    private val _contactsFragment by lazy { choolooFragmentFactory.getContactsFragment() }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var screens: ScreensInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var choolooFragmentFactory: ChoolooFragmentFactory


    override fun onSetup() {
        screens.disableKeyboard()

        binding.apply {
            mainTabs.viewPager = mainViewPager
            mainTabs.setHeadersResList(arrayOf(R.string.contacts, R.string.recents))

            mainMenuButton.setOnClickListener {
                viewState.onMenuClick()
            }

            mainDialpadButton.setOnClickListener {
                viewState.onDialpadFabClick()
            }

            mainSearchBar.setOnTextChangedListener { st ->
                contactsViewState.onFilterChanged(st)
                recentsViewState.onFilterChanged(st)
            }

            mainSearchBar.editText?.setOnFocusChangeListener { _, hasFocus ->
                viewState.onSearchFocusChange(hasFocus)
            }

            mainViewPager.adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount() = _fragments.size
                override fun createFragment(position: Int) = _fragments[position]
            }

            mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewState.onPageSelected(position)
                }
            })
        }

        viewState.apply {
            searchText.observe(this@MainActivity) {
                binding.mainSearchBar.text = it
            }

            searchHint.observe(this@MainActivity) {
                binding.mainSearchBar.hint = it.toString()
            }

            currentPageIndex.observe(this@MainActivity) {
                binding.mainViewPager.currentItem = it
            }

            isSearching.observe(this@MainActivity) {
                if (it) binding.root.transitionToState(R.id.constraint_set_main_collapsed)
            }

            showMenuEvent.observe(this@MainActivity) { ev ->
                ev.ifNew?.let { prompts.showFragment(fragmentFactory.getSettingsFragment()) }
            }

            showDialerEvent.observe(this@MainActivity) { ev ->
                ev.ifNew?.let {
                    prompts.showFragment(choolooFragmentFactory.getDialerFragment())
                    _dialerViewState.onTextChanged(it)
                }
            }
        }

        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            viewState.onViewIntent(intent)
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        screens.ignoreEditTextFocus(event)
        return super.dispatchTouchEvent(event)
    }
}