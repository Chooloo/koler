package com.chooloo.www.koler.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.FragmentSearchBarBinding
import com.chooloo.www.koler.ui.base.BaseFragment

class SearchFragment : BaseFragment(), SearchMvpView {

    private var _onTextChangedListener: ((text: String) -> Unit?)? = null
    private lateinit var _presenter: SearchMvpPresenter<SearchMvpView>
    private lateinit var _binding: FragmentSearchBarBinding

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override val text: String
        get() = _binding.searchInputEditText.text.toString()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBarBinding.inflate(inflater)
        return _binding.root
    }

    override fun onSetup() {
        _presenter = SearchPresenter()
        _presenter.attach(this)

        _binding.searchInputEditText.addTextChangedListener(
                afterTextChanged = {},
                onTextChanged = { _, _, _, _ -> },
                beforeTextChanged = { s, _, _, _ ->
                    _presenter.onTextChanged(s.toString())
                    _onTextChangedListener?.invoke(s.toString())
                }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun setFocus() {
        _binding.searchInputEditText.requestFocus()
    }

    override fun showIcon(isShow: Boolean) {
        _binding.searchInputEditText.setCompoundDrawablesWithIntrinsicBounds(if (isShow) ContextCompat.getDrawable(_activity, R.drawable.ic_search_black_24dp) else null, null, null, null)
    }

    fun setOnTextChangedListener(onTextChangedListener: ((text: String) -> Unit?)?) {
        _onTextChangedListener = onTextChangedListener
    }
}