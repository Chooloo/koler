package com.chooloo.www.chooloolib.utils

class SelectionBuilder {
    private val selections = arrayListOf<String>()
    private val args = arrayListOf<String>()

    fun addSelection(key: String, value: Any?) = apply {
        value?.let {
            selections.add("$key = ?")
            args.add(it.toString())
        }
    }

    fun addNotNull(key: String) = apply {
        selections.add("$key IS NOT NULL")
    }

    fun addRaw(clause: String, vararg clauseArgs: String) = apply {
        if (clause.isNotEmpty()) {
            selections.add(clause)
            args.addAll(clauseArgs)
        }
    }

    fun build(): String? = selections.takeIf { it.isNotEmpty() }?.joinToString(" AND ")

    fun buildArgs(): Array<String>? = args.takeIf { it.isNotEmpty() }?.toTypedArray()
}
