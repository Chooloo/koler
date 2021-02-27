package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class MainPresenter<V : MainMvpView> : BasePresenter<V>(), MainMvpPresenter<V> {
    override fun onDialpadFabClick() {
        mvpView?.openDialpad()
    }

    override fun onOptionsItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_settings -> mvpView?.goToSettings()
            R.id.action_about -> mvpView?.goToAbout()
        }
    }

    override fun onMenuClick() {
        mvpView?.openMenu()
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
                openDialpad()
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