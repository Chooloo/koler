package com.chooloo.www.callmanager.ui.page

import com.chooloo.www.callmanager.ui.recents.RecentsFragment

class PageRecents : PageFragment(), PageMvpView {

    private lateinit var _presenter: PageMvpPresenter<PageMvpView>
    private lateinit var _recentsFragment: RecentsFragment

    companion object {
        fun newInstance(): PageRecents = PageRecents()
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = PagePresenter()
        _presenter.attach(this)

        _recentsFragment = RecentsFragment.newInstance().apply {
            setOnScrollStateChangedListener { newState -> _presenter.onScrollStateChanged(newState) }
        }

        childFragmentManager.beginTransaction().replace(binding.fragmentPageLayout.id, _recentsFragment).commitNow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun onSearchTextChanged(text: String) {
        _presenter.onSearchTextChanged(text)
    }

    override fun onDialNumberChanged(number: String) {
        _presenter.onDialNumberChanged(number)
    }
}