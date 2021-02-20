package com.chooloo.www.koler.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.lookupContact
import com.chooloo.www.koler.util.setFadeUpAnimation

class RecentsAdapter(
    private val context: Context,
) : RecyclerView.Adapter<ListItemHolder>() {

    private var _recents: Array<Recent> = arrayOf()
    private var _onRecentItemClickListener: ((Recent) -> Unit?)? = null
    private var _onRecentItemLongClickListener: ((Recent) -> Unit?)? = null

    override fun getItemCount() = _recents.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(parent.context)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val recent = _recents[position]
        val contact = context.lookupContact(recent.number)

        holder.listItem.run {
            bigText = contact.name ?: recent.number
            smallText = if (recent.date != null) recent.relativeTime else null
            imageDrawable = ContextCompat.getDrawable(context, getCallTypeImage(recent.type))
            
            setImageBackgroundColor(Color.TRANSPARENT)
            setOnClickListener { _onRecentItemClickListener?.invoke(recent) }
            setOnLongClickListener {
                _onRecentItemLongClickListener?.invoke(recent)
                true
            }
            setFadeUpAnimation(this)
        }
    }

    fun updateRecents(newRecents: Array<Recent>) {
        _recents = newRecents
        notifyDataSetChanged()
    }

    fun setOnRecentItemClickListener(onRecentItemClickListener: ((Recent) -> Unit?)?) {
        _onRecentItemClickListener = onRecentItemClickListener
    }

    fun setOnRecentItemLongClickListener(onRecentItemLongClickListener: ((Recent) -> Unit?)?) {
        _onRecentItemLongClickListener = onRecentItemLongClickListener
    }
}