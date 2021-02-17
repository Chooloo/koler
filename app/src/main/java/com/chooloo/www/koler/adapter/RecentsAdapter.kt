package com.chooloo.www.koler.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.RelativeTime.getTimeAgo
import com.chooloo.www.koler.util.lookupContact
import com.chooloo.www.koler.util.setFadeUpAnimation

class RecentsAdapter(
        private val context: Context,
) : RecyclerView.Adapter<ListItemHolder>() {

    private var _recents: Array<Recent> = arrayOf()

    private var _onRecentItemClickListener: ((Recent) -> Unit?)? = null
    private var _onRecentItemLongClickListener: ((Recent) -> Unit?)? = null

    override fun getItemCount(): Int {
        return _recents.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(parent.context)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val recentCall = _recents[position]
        val contact = context.lookupContact(recentCall.number)

        holder.listItem.run {
            setBigText(contact.name ?: recentCall.number)
            setSmallText(if (recentCall.date != null) getTimeAgo(recentCall.date.time) else null)
            setImageDrawable(ContextCompat.getDrawable(context, getCallTypeImage(recentCall.type)))
            setImageBackgroundColor(Color.TRANSPARENT)
            setOnClickListener { _onRecentItemClickListener?.invoke(recentCall) }
            setOnLongClickListener {
                _onRecentItemLongClickListener?.invoke(recentCall)
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