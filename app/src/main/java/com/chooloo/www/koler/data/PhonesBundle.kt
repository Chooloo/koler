package com.chooloo.www.koler.data

import PhoneAccount
import android.content.Context

data class PhonesBundle(
    val phoneAccounts: Array<PhoneAccount>
) {
    fun getListBundleByType(context: Context) = ListBundle(
        items = phoneAccounts,
        headers = phoneAccounts.groupBy { it.type }.keys.map { context.getString(it.stringRes) }
            .toTypedArray(),
        headersCounts = phoneAccounts.groupingBy { it.type }.eachCount().values.toTypedArray()
    )
}