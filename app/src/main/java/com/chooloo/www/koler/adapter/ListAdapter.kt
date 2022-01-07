package com.chooloo.www.koler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.list.ListData
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.ui.widgets.listitem.ListItemHolder
import com.l4digital.fastscroll.FastScroller

abstract class ListAdapter<ItemType>(
    protected val component: ActivityComponent
) : RecyclerView.Adapter<ListItemHolder>(), FastScroller.SectionIndexer {
    private var _data: ListData<ItemType> = ListData()
    private var _onItemClickListener: (item: ItemType) -> Unit = {}
    private var _onItemLongClickListener: (item: ItemType) -> Unit = {}

    var items: List<ItemType>
        get() = _data.items
        set(value) {
            _data = convertDataToListData(value)
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(parent.context)

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val dataItem = getItem(position)
        holder.listItem.apply {
            headerText = getHeader(position)

            setOnClickListener {
                _onItemClickListener.invoke(dataItem)
            }
            setOnLongClickListener {
                _onItemLongClickListener.invoke(dataItem)
                true
            }
            component.animations.show(this, false)

            onBindListItem(this, dataItem)
        }
    }

    override fun getItemCount() = _data.items.size

    override fun getSectionText(position: Int): String? {
        var total = 0
        _data.headersToCounts.values.withIndex().forEach { (index, count) ->
            if (position <= total) {
                return _data.headersToCounts.keys.toList()[index]
            } else {
                total += count
            }
        }
        return null
    }

    private fun getItem(position: Int) = _data.items[position]

    fun getHeader(position: Int): String? {
        var total = 0
        _data.headersToCounts.values.withIndex().forEach { (index, count) ->
            when (position) {
                total -> return _data.headersToCounts.keys.toList()[index]
                else -> total += count
            }
        }
        return null
    }

    fun setOnItemClickListener(onItemClickListener: (item: ItemType) -> Unit) {
        _onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (item: ItemType) -> Unit) {
        _onItemLongClickListener = onItemLongClickListener
    }


    abstract fun onBindListItem(listItem: ListItem, item: ItemType)
    abstract fun convertDataToListData(data: List<ItemType>): ListData<ItemType>
}