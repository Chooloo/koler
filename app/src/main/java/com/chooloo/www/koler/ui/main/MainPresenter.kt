package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class MainPresenter<V : MainMvpView> : BasePresenter<V>(), MainMvpPresenter<V> {
    override fun onDialpadFabClick() {
        mvpView?.isDialpadVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_settings -> mvpView?.goToSettings()
            R.id.action_about -> mvpView?.goToAbout()
        }
    }

    override fun onMenuClick() {
        mvpView?.isMenuVisible = true
    }

    override fun onViewIntent(intent: Intent) {
        val intentText = try {
            URLDecoder.decode(intent.dataString, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            mvpView?.showError("An error occurred when trying to get phone number :(")
            return
        }

        if (intentText.contains("tel:")) {
            mvpView?.apply {
                isDialpadVisible = true
                dialpadNumber = intentText
            }
        } else {
            mvpView?.showError("No phone number detected")
        }
    }

    override fun onSearchTextChanged(text: String) {
        mvpView?.updateSearchViewModelText(text)
    }
}