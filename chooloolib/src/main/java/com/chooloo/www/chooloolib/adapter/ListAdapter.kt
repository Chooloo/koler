package com.chooloo.www.chooloolib.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItemHolder
import com.l4digital.fastscroll.FastScroller

abstract class ListAdapter<ItemType>(
    private val animations: AnimationsInteractor
) : RecyclerView.Adapter<ListItemHolder>(), FastScroller.SectionIndexer {
    private var _isCompact: Boolean = false
    private var _titleFilter: String? = null
    private var _data: ListData<ItemType> = ListData()
    private var _onItemClickListener: (item: ItemType) -> Unit = {}
    private var _onItemLongClickListener: (item: ItemType) -> Unit = {}
    private var _onItemLeftButtonClickListener: (item: ItemType) -> Unit = {}
    private var _onItemRightButtonClickListener: (item: ItemType) -> Unit = {}


    var titleFilter: String?
        get() = _titleFilter
        set(value) {
            _titleFilter = value
        }

    var items: List<ItemType>
        get() = _data.items
        set(value) {
            _data = convertDataToListData(value)
            notifyDataSetChanged()
        }

    var isCompact: Boolean
        get() = _isCompact
        set(value) {
            _isCompact = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(parent.context)

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val dataItem = getItem(position)
        holder.listItem.apply {
            isCompact = _isCompact
            headerText = getHeader(position)

            setOnClickListener { _onItemClickListener.invoke(dataItem) }
            setOnLeftButtonClickListener { _onItemLeftButtonClickListener.invoke(dataItem) }
            setOnRightButtonClickListener { _onItemRightButtonClickListener.invoke(dataItem) }
            setOnLongClickListener {
                _onItemLongClickListener.invoke(dataItem)
                true
            }

            animations.show(this, false)

            onBindListItem(this, dataItem)

            _titleFilter?.let { highlightTitleText(it) }
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

    private fun getHeader(position: Int): String? {
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

    fun setOnItemLeftButtonClickListener(onItemLeftButtonClickListener: (item: ItemType) -> Unit) {
        _onItemLeftButtonClickListener = onItemLeftButtonClickListener
    }

    fun setOnItemRightButtonClickListener(onItemRightButtonClickListener: (item: ItemType) -> Unit) {
        _onItemRightButtonClickListener = onItemRightButtonClickListener
    }


    open fun convertDataToListData(data: List<ItemType>) = ListData(data)

    abstract fun onBindListItem(listItem: ListItem, item: ItemType)
}