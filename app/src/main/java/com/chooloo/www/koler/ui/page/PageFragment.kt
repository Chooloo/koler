package com.chooloo.www.koler.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chooloo.www.koler.databinding.FragmentPageBinding
import com.chooloo.www.koler.ui.base.BaseFragment
import com.chooloo.www.koler.viewmodel.SearchViewModel

abstract class PageFragment : BaseFragment(), PageMvpView {

    private lateinit var _presenter: PageMvpPresenter<PageMvpView>
    private lateinit var _searchViewModel: SearchViewModel
    protected lateinit var binding: FragmentPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPageBinding.inflate(inflater)
        return binding.root
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
}