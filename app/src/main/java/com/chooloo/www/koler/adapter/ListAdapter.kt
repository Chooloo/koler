package com.chooloo.www.koler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.ui.widgets.ListItemHolder

abstract class ListAdapter<DataType>(
    protected val boundComponent: BoundComponentRoot
) : ListAdapter<DataType, ListItemHolder>(object : DiffUtil.ItemCallback<DataType>() {
    override fun areItemsTheSame(oldItem: DataType, newItem: DataType) = oldItem == newItem
    override fun areContentsTheSame(oldItem: DataType, newItem: DataType) = oldItem == newItem
}) {
    private var _isCompact = false
    private var _isSelecting = false
    private var _isSelectable = true
    private var _data: ListBundle<DataType> = ListBundle()
    private var _selectedItems: ArrayList<DataType> = arrayListOf()

    private var _onItemClickListener: (item: DataType) -> Unit = {}
    private var _onItemLongClickListener: (item: DataType) -> Unit = {}
    private var _onSelectingChangeListener: (isSelecting: Boolean) -> Unit = {}
    private var _onItemsSelectedListener: (items: ArrayList<DataType>) -> Unit = {}

    var data: ListBundle<DataType>
        get() = _data
        set(value) {
            _data = value
            submitList(_data.items.toList())
        }

    var isCompact
        get() = _isCompact
        set(value) {
            _isCompact = value
        }

    val isSelecting: Boolean
        get() = _isSelecting

    val selectedItems: ArrayList<DataType>
        get() = _selectedItems


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(parent.context)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val dataItem = getItem(position)
        holder.listItem.apply {
            headerText = getHeader(position)
            isCompact = this@ListAdapter.isCompact

            setOnClickListener {
                if (isSelected && _isSelectable) {
                    isSelected = false
                    _selectedItems.remove(dataItem)
                    if (_selectedItems.size == 0) {
                        _isSelecting = false
                        _onSelectingChangeListener.invoke(false)
                    }
                } else if (!isSelected && _isSelecting) {
                    isSelected = true
                    _selectedItems.add(dataItem)
                    _onItemsSelectedListener.invoke(_selectedItems)
                } else {
                    _onItemClickListener.invoke(getItem(position))
                }
            }
            setOnLongClickListener {
                if (_isSelectable) {
                    isSelected = true
                    _isSelecting = true
                    _selectedItems.add(dataItem)
                    _onItemsSelectedListener.invoke(_selectedItems)
                    _onSelectingChangeListener.invoke(true)
                } else {
                    _onItemLongClickListener.invoke(dataItem)
                }
                true
            }
            boundComponent.animationInteractor.animateIn(this, false)
            onBindListItem(this, getItem(position))
        }
    }

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


    //region listeners setters

    fun setOnItemClickListener(onItemClickListener: (item: DataType) -> Unit) {
        _onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (item: DataType) -> Unit) {
        _onItemLongClickListener = onItemLongClickListener
    }

    fun setOnSelectingChangeListener(onSelectingChangeListener: (isSelecting: Boolean) -> Unit) {
        _onSelectingChangeListener = onSelectingChangeListener
    }

    fun setOnItemsSelectedListener(onItemsSelectedListener: (items: ArrayList<DataType>) -> Unit) {
        _onItemsSelectedListener = onItemsSelectedListener
    }

    //endregion


    abstract fun onBindListItem(listItem: ListItem, item: DataType)
}