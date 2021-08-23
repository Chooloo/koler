package com.chooloo.www.koler.adapter

import PhoneAccount
import android.content.Context
import android.telecom.TelecomManager
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.numbers.NumbersInteractorImpl
import com.chooloo.www.koler.interactor.permission.PermissionInteractorImpl
import com.chooloo.www.koler.ui.widgets.ListItem

class PhonesAdapter(
    private val context: Context
) : ListAdapter<PhoneAccount>() {
    private val _permissionInteractor by lazy {
        PermissionInteractorImpl(
            context,
            context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        )
    }
    private val _numbersInteractor by lazy { NumbersInteractorImpl(context) }
    private val _blockedDrawable by lazy {
        ContextCompat.getDrawable(context, R.drawable.round_block_black_24)
    }

    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            titleText = item.number
            imageVisibility = false

            _permissionInteractor.runWithDefaultDialer {
                if (_numbersInteractor.isNumberBlocked(item.number)) {
                    imageDrawable = _blockedDrawable
                }
            }
        }
    }
}