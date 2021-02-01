package com.chooloo.www.callmanager.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.viewmodel.CursorsViewModel

class CursorsViewModelFactory(
        private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CursorsViewModel::class.java)) {
            return CursorsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}