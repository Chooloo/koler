package com.chooloo.www.koler.ui.main

import android.content.Intent
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseController
import java.net.URLDecoder

class MainController<V : MainContract.View>(view: V) :
    BaseController<V>(view),
    MainContract.Controller<V> {

    override fun onMenuClick() {
        view.openSettings()
    }

    override fun onDialpadFabClick() {
        view.openDialpad()
    }

    override fun onViewIntent(intent: Intent) {
        val intentText = try {
            URLDecoder.decode(intent.dataString ?: "", "utf-8")
        } catch (e: Exception) {
            view.showError(R.string.error_couldnt_get_number_from_intent)
            return
        }

        if (intentText.contains("tel:")) {
            view.openDialpad()
            view.dialpadNumber = intentText.substringAfter("tel:")
        } else {
            view.showError(R.string.error_couldnt_get_number_from_intent)
        }
    }
}