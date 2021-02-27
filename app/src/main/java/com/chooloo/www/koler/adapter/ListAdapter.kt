package com.chooloo.www.koler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.setFadeUpAnimation

abstract class ListAdapter<DataType> : RecyclerView.Adapter<ListItemHolder>() {
    private var _data: ListBundle<DataType> = ListBundle()
    private var _onItemClickListener: (item: DataType) -> Unit? = {}
    private var _onItemLongClickListener: (item: DataType) -> Unit? = {}

    var data: ListBundle<DataType>
        get() = _data
        set(value) {
            _data = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = _data.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(parent.context)

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        holder.listItem.apply {
            headerText = getHeader(position)
            setFadeUpAnimation(this)
            setOnClickListener { _onItemClickListener.invoke(_data.items[position]) }
            setOnLongClickListener {
                _onItemLongClickListener.invoke(_data.items[position])
                true
            }
            onBindListItem(this, _data.items[position])
        }
    }

    private fun getHeader(position: Int): String? {
        var total = 0
        _data.headersCounts.withIndex().forEach { (index, count) ->
            when (position) {
                total -> return _data.headers[index]
                else -> total += count
            }
        }
        return null
    }

    fun setOnItemClickListener(onItemClickListener: (item: DataType) -> Unit) {
        _onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (item: DataType) -> Unit) {
        _onItemLongClickListener = onItemLongClickListener
    }

    abstract fun onBindListItem(listItem: ListItem, item: DataType)
}