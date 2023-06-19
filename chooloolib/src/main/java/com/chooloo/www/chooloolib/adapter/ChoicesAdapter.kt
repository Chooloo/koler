package com.chooloo.www.chooloolib.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chooloo.www.chooloolib.data.model.ListData
import com.chooloo.www.chooloolib.databinding.ListItemBinding
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ChoiceItemHolder
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import javax.inject.Inject

@SuppressLint("RestrictedApi")
class ChoicesAdapter @Inject constructor(
    animations: AnimationsInteractor,
) : ListAdapter<String>(animations) {
    private var _selectedIndex: Int? = null
    private var _onChoiceSelectedListener: (String) -> Boolean = { true }

    var selectedIndex: Int?
        get() = _selectedIndex
        set(value) {
            _selectedIndex = if (value == -1) null else value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChoiceItemHolder(
        ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindListItem(listItemHolder: ListItemHolder, item: String, position: Int) {
        listItemHolder.apply {
            titleText = item
            (this as ChoiceItemHolder).setSelected(selectedIndex == position)
            setOnClickListener {
                if (_onChoiceSelectedListener.invoke(item)) {
                    selectedIndex?.let(::notifyItemChanged)
                    selectedIndex = position
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun convertDataToListData(items: List<String>) = ListData(items)

    fun setOnChoiceSelectedListener(onChoiceSelectedListener: (String) -> Boolean) {
        _onChoiceSelectedListener = onChoiceSelectedListener
    }
}