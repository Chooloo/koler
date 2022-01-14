package com.chooloo.www.chooloolib.util

open class SelectionBuilder {
    private val selections = arrayListOf<String>()

    fun addSelection(key: String, value: Any?) = this.also {
        value?.let { selections.add("$key = $value") }
    }

    fun addNotNull(key: String) = this.also {
        selections.add("$key IS NOT NULL")
    }

    fun addString(string: String) = this.also {
        selections.add(string)
    }

    fun build() = selections.joinToString(" AND ")
}