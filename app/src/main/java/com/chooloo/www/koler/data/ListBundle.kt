package com.chooloo.www.koler.data

data class ListBundle<DataType>(
    val items: Array<DataType> = arrayOfNulls<Any>(0) as Array<DataType>,
    val headers: Array<String> = arrayOf(),
    val headersCounts: Array<Int> = arrayOf()
)