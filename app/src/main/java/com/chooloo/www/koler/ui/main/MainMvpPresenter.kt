package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MenuItem
import com.chooloo.www.koler.ui.base.MvpPresenter

interface MainMvpPresenter<V : MainMvpView> : MvpPresenter<V> {
    fun onDialpadFabClick()
    fun onSearchTextChanged(text: String)
    fun onSearchFocusChanged(isFocused: Boolean)
    fun onDialNumberChanged(number: String)
    fun onOptionsItemSelected(item: MenuItem)
    fun onBackPressed(): Boolean
    fun onMenuClick()
    fun onViewIntent(intent: Intent)
}