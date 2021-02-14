package com.chooloo.www.koler.adapter

import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.ui.widgets.ListItemHolder

class MenuAdapter(
        private val _context: Context,
        private val _menu: Menu,
) : RecyclerView.Adapter<ListItemHolder>() {
    private var _onMenuItemClickListener: ((MenuItem) -> Unit?)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(_context)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val menuItem = _menu.getItem(position)
        holder.listItem.apply {
            setOnClickListener { _onMenuItemClickListener?.invoke(menuItem) }
            setBigText(menuItem.title.toString())
            setImageDrawable(menuItem.icon)
        }
    }

    override fun getItemCount(): Int {
        return _menu.size()
    }

    fun setMenu(menu: Menu) {

    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: ((MenuItem) -> Unit?)?) {
        _onMenuItemClickListener = onMenuItemClickListener
    }
}