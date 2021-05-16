package com.chooloo.www.koler.data

data class ListBundle<DataType>(
    val items: ArrayList<DataType> = arrayListOf(),
    val headers: Array<String> = arrayOf(),
    val headersCounts: Array<Int> = arrayOf()
)