package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.koler.ui.base.BasePresenter
import java.net.URLDecoder

class MainPresenter<V : MainContract.View>(view: V) :
    BasePresenter<V>(view),
    MainContract.Presenter<V> {

    override fun onMenuClick() {
        view.openSettings()
    }

    override fun onDialpadFabClick() {
        view.openDialpad()
    }

    override fun onViewIntent(intent: Intent) {
        val intentText = try {
            URLDecoder.decode(intent.dataString, "utf-8")
        } catch (e: Exception) {
            view.showError("An error occurred when trying to get phone number :(")
            return
        }

        if (intentText.contains("tel:")) {
            view.dialpadNumber = intentText
            view.openDialpad()
        } else {
            view.showError("No phone number detected")
        }
    }

    override fun onDialpadTextChanged(text: String?) {
        view.updateSearchViewModelNumber(text)
    }
}