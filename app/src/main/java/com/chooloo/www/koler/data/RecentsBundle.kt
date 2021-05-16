package com.chooloo.www.koler.data

import com.chooloo.www.koler.util.getRelativeDateString


data class RecentsBundle(
    val recents: ArrayList<Recent>
) {
    val listBundleByDates: ListBundle<Recent>
        get() {
            if (recents.isEmpty()) {
                return ListBundle()
            }
            val headers: ArrayList<String> = arrayListOf()
            val headersCounts: ArrayList<Int> = arrayListOf()
            var currentCount = 0
            var currentHeader = getRelativeDateString(recents[0].date)
            recents.forEach {
                val dateString = getRelativeDateString(it.date)
                if (dateString != currentHeader) {
                    headers.add(currentHeader)
                    headersCounts.add(currentCount)
                    currentHeader = dateString
                    currentCount = 1
                } else {
                    currentCount = currentCount.plus(1)
                }
            }
            return ListBundle(
                items = recents,
                headers = headers.toTypedArray(),
                headersCounts = headersCounts.toTypedArray()
            )
        }
}

