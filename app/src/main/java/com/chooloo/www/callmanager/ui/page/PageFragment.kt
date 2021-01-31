package com.chooloo.www.callmanager.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.databinding.FragmentPageBinding
import com.chooloo.www.callmanager.ui.base.BaseFragment
import com.chooloo.www.callmanager.viewmodel.DialViewModel
import com.chooloo.www.callmanager.viewmodel.SharedSearchViewModel

abstract class PageFragment : BaseFragment(), PageMvpView {
    private lateinit var _presenter: PageMvpPresenter<PageMvpView>
    protected lateinit var _binding: FragmentPageBinding
    protected lateinit var _dialViewModel: DialViewModel
    protected lateinit var _searchViewModel: SharedSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _dialViewModel = ViewModelProvider(this).get(DialViewModel::class.java)
        _dialViewModel.number.observe(this, Observer { number: String? -> onDialNumberChanged(number) })

        _searchViewModel = ViewModelProvider(this).get(SharedSearchViewModel::class.java)
        _searchViewModel.text.observe(this, Observer { text: String? -> onSearchTextChanged(text) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPageBinding.inflate(inflater)
        return _binding.root
    }

    override fun onSetup() {
        _presenter = PagePresenter()
        _presenter.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun setSearchBarFocused(isFocused: Boolean) {
        _searchViewModel.isFocused.value = isFocused
    }

    protected abstract fun onSearchTextChanged(text: String?)
    protected abstract fun onDialNumberChanged(number: String?)
}