package com.chooloo.www.callmanager.livedata

import android.content.Context
import com.chooloo.www.callmanager.contentresolver.RecentsContentResolver
import com.chooloo.www.callmanager.entity.Recent

class RecentsLiveData(context: Context) : BaseContentLiveData<RecentsContentResolver, Array<Recent>>(context) {

    companion object {
        val REQUIRED_PERMISSION = RecentsContentResolver.REQUIRED_PERMISSION
    }

    override fun onGetContentResolver(): RecentsContentResolver {
        return RecentsContentResolver(context)
    }
}