package com.chooloo.www.kontacts.ui.main

import androidx.activity.viewModels
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseActivity
import com.chooloo.www.kontacts.R
import com.chooloo.www.kontacts.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory as ChoolooFragmentFactory

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: MainViewState by viewModels()

    private val binding by lazy { MainBinding.inflate(layoutInflater) }
    private val contactsFragment by lazy { fragmentFactory.getContactsFragment() }

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var fragmentFactory: ChoolooFragmentFactory


    override fun onSetup() {
        binding.apply {
            mainTabs.setHeadersResList(arrayOf(R.string.contacts))

            mainMenuButton.setOnClickListener {
                viewState.onSettingsClick()
            }

            mainAddContactButton.setOnClickListener {
                viewState.onAddContactClick()
            }

            mainSearchBar.apply {
                setOnTextChangedListener(viewState::onSearchTextChange)
                editText?.setOnFocusChangeListener { _, hasFocus ->
                    viewState.onSearchFocusChange(hasFocus)
                }
            }
        }

        viewState.apply {
            searchHintRes.observe(this@MainActivity, binding.mainSearchBar::setHint)

            searchText.observe(this@MainActivity) {
                binding.mainSearchBar.text = it
            }

            isSearching.observe(this@MainActivity) {
                if (it) binding.root.transitionToState(R.id.constraint_set_main_collapsed)
            }

            showMenuEvent.observe(this@MainActivity) {
                it.ifNew?.let { prompts.showFragment(fragmentFactory.getSettingsFragment()) }
            }
        }

        supportFragmentManager
            .beginTransaction()
            .replace(binding.mainFragmentContainer.id, contactsFragment)
            .commit()

        viewState.onIntent(intent)
    }
}