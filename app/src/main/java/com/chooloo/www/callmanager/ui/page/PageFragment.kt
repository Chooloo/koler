package com.chooloo.www.callmanager.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.databinding.FragmentPageBinding
import com.chooloo.www.callmanager.ui.base.BaseFragment
import com.chooloo.www.callmanager.viewmodel.dial.DialViewModel
import com.chooloo.www.callmanager.viewmodel.search.SearchViewModel

abstract class PageFragment : BaseFragment(), PageMvpView {

    private lateinit var _presenter: PageMvpPresenter<PageMvpView>
    private lateinit var _dialViewModel: DialViewModel
    private lateinit var _searchViewModel: SearchViewModel
    protected lateinit var binding: FragmentPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onSetup() {
        _presenter = PagePresenter()
        _presenter.attach(this)

        _dialViewModel = ViewModelProvider(this).get(DialViewModel::class.java).apply {
            number.observe(viewLifecycleOwner, { number: String? ->
                onDialNumberChanged(number ?: "")
            })
        }

        _searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java).apply {
            text.observe(viewLifecycleOwner, { text: String? ->
                onSearchTextChanged(text ?: "")
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun setSearchBarFocused(isFocused: Boolean) {
        _searchViewModel.isFocused.value = isFocused
    }

    protected abstract fun onSearchTextChanged(text: String)
    protected abstract fun onDialNumberChanged(number: String)
}