package com.chooloo.www.koler.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.entity.Recent.CallType
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.RelativeTime.getTimeAgo
import com.chooloo.www.koler.util.lookupContact
import com.chooloo.www.koler.util.setFadeUpAnimation

class RecentsAdapter(
        private val context: Context
) : RecyclerView.Adapter<ListItemHolder>() {

    private var _recents: Array<Recent> = arrayOf()

    private var _onRecentItemClickListener: ((Recent) -> Unit?)? = null
    private var _onRecentItemLongClickListener: ((Recent) -> Unit?)? = null

    override fun getItemCount(): Int {
        return _recents.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(ListItem(parent.context))
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val recentCall = _recents.get(position)
        val contact = lookupContact(context, recentCall.callerNumber)

        holder.listItem.run {
            setBigText(contact.name ?: recentCall.callerNumber)
            setSmallText(if (recentCall.callDate != null) getTimeAgo(recentCall.callDate.getTime()) else null)
            setImageDrawable(ContextCompat.getDrawable(context, getCallTypeImage(recentCall.callType)))
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

    fun getCallTypeImage(@CallType callType: Int): Int {
        return when (callType) {
            Recent.TYPE_INCOMING -> R.drawable.ic_call_received_black_24dp
            Recent.TYPE_OUTGOING -> R.drawable.ic_call_made_black_24dp
            Recent.TYPE_MISSED -> R.drawable.ic_call_missed_black_24dp
            Recent.TYPE_REJECTED -> R.drawable.ic_call_missed_outgoing_black_24dp
            Recent.TYPE_VOICEMAIL -> R.drawable.ic_voicemail_black_24dp
            else -> R.drawable.ic_call_made_black_24dp
        }
    }
}