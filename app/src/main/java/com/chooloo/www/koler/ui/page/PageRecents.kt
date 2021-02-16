package com.chooloo.www.koler.ui.page

import com.chooloo.www.koler.ui.recents.RecentsFragment

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
            setOnScrollStateChangedListener(_presenter::onScrollStateChanged)
        }

        childFragmentManager.beginTransaction().replace(binding.fragmentPageLayout.id, _recentsFragment).commitNow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }
}