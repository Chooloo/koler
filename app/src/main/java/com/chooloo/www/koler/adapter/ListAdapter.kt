package com.chooloo.www.koler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.entity.ListBundle
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.setFadeUpAnimation

abstract class ListAdapter<DataType> : RecyclerView.Adapter<ListItemHolder>() {
    private lateinit var _listBundle: ListBundle<DataType>
    private var _onItemClickListener: (item: DataType) -> Unit? = {}
    private var _onItemLongClickListener: (item: DataType) -> Unit? = {}

    var data: ListBundle<DataType>
        get() = _listBundle
        set(value) {
            _listBundle = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = _listBundle.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(parent.context)

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        holder.listItem.apply {
            headerText = getHeader(position)
            setFadeUpAnimation(this)
            setOnClickListener { _onItemClickListener.invoke(_listBundle.items[position]) }
            setOnLongClickListener {
                _onItemLongClickListener.invoke(_listBundle.items[position])
                true
            }
            onBindListItem(this, _listBundle.items[position])
        }
    }

    private fun getHeader(position: Int): String? {
        var total = 0
        _listBundle.headersCounts.withIndex().forEach { (index, count) ->
            when (position) {
                total -> return _listBundle.headers[index]
                else -> total += count
            }
        }
        return null
    }

    abstract fun onBindListItem(listItem: ListItem, item: DataType)


}