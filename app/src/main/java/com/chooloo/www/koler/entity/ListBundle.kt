package com.chooloo.www.koler.entity

data class ListBundle<DataType>(
    val items: Array<DataType>,
    val headers: Array<String> = arrayOf(),
    val headersCounts: Array<Int> = arrayOf()
)