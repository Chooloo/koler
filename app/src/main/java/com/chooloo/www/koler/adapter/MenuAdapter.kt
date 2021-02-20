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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListItemHolder(_context)
    
    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val menuItem = _menu.getItem(position)
        holder.listItem.apply {
            setOnClickListener { _onMenuItemClickListener?.invoke(menuItem) }
            bigText = menuItem.title.toString()
            imageDrawable = menuItem.icon
        }
    }

    override fun getItemCount() = _menu.size()

    fun setOnMenuItemClickListener(onMenuItemClickListener: ((MenuItem) -> Unit?)?) {
        _onMenuItemClickListener = onMenuItemClickListener
    }
}