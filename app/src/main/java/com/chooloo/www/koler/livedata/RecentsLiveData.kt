package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.entity.Recent

class RecentsLiveData(context: Context) : BaseContentLiveData<RecentsContentResolver, Array<Recent>>(context) {

    companion object {
        val REQUIRED_PERMISSION = RecentsContentResolver.REQUIRED_PERMISSION
    }

    override fun onGetContentResolver(): RecentsContentResolver {
        return RecentsContentResolver(context)
    }
}