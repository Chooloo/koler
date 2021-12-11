package com.chooloo.www.koler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.ui.widgets.listitem.ListItemHolder

abstract class ListAdapter<DataType>(
    protected val component: ActivityComponent
) : RecyclerView.Adapter<ListItemHolder>() {
    private var _data: ListBundle<DataType> = ListBundle()
    private var _onItemClickListener: (item: DataType) -> Unit = {}
    private var _onItemLongClickListener: (item: DataType) -> Unit = {}


    var data: ListBundle<DataType>
        get() = _data
        @Synchronized
        set(value) {
            _data = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(parent.context)

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val dataItem = getItem(position)
        holder.listItem.apply {
            headerText = getHeader(position)

            setOnClickListener { _onItemClickListener.invoke(dataItem) }
            setOnLongClickListener {
                _onItemLongClickListener.invoke(dataItem)
                true
            }
            component.animationInteractor.animateIn(this, false)

            onBindListItem(this, dataItem)
        }
    }

    override fun getItemCount() = _data.items.size

    private fun getItem(position: Int) = _data.items[position]


    fun getHeader(position: Int): String? {
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