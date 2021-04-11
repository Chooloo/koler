package com.chooloo.www.koler.data

import PhoneAccount
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone

data class PhonesBundle(
    val phoneAccounts: Array<PhoneAccount>
) {
    fun getListBundleByType(
        context: Context,
        distinctNormalizedNumber: Boolean = false
    ): ListBundle<PhoneAccount> {
        val items = if (distinctNormalizedNumber) {
            phoneAccounts.toList().distinctBy { it.normalizedNumber }.toTypedArray()
        } else {
            phoneAccounts
        }
        return ListBundle(
            items = items,
            headers = items.groupBy { it.type }.keys.map {
                context.getString(Phone.getTypeLabelResource(it))
            }.toTypedArray(),
            headersCounts = items.groupingBy { it.type }.eachCount().values.toTypedArray()
        )
    }
}