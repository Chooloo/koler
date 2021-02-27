package com.chooloo.www.koler.data

data class RecentsBundle(
    val recents: Array<Recent>
) {
    val listBundleByDates: ListBundle<Recent>
        get() {
            val headers: ArrayList<String> = arrayListOf()
            val headersCounts: ArrayList<Int> = arrayListOf()
            var currentCount = 0
            var currentHeader: String? = null
            recents.forEach {
                if (it.relativeTime != currentHeader) {
                    headers.add(currentHeader ?: "")
                    headersCounts.add(currentCount)
                    currentHeader = it.relativeTime
                    currentCount = 1
                } else {
                    currentCount.plus(1)
                }
            }
            return ListBundle(
                items = recents,
                headers = headers.toTypedArray(),
                headersCounts = headersCounts.toTypedArray()
            )
        }
}

