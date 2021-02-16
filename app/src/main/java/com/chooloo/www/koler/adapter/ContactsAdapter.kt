package com.chooloo.www.koler.adapter

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.setFadeUpAnimation

open class ContactsAdapter : RecyclerView.Adapter<ListItemHolder>() {

    private var _contacts: Array<Contact> = arrayOf()
    private var _onContactItemClickListener: ((Contact) -> Unit?)? = null
    private var _onContactItemLongClickListener: ((Contact) -> Unit)? = null

    private val headersCounts: Array<Int>
        get() = arrayOf()

    private val headers: Array<String>
        get() = arrayOf()

    override fun getItemCount(): Int {
        return _contacts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        return ListItemHolder(parent.context)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val contact = _contacts.get(position)
        holder.listItem.apply {
            setBigText(contact.name)
            setHeaderText(getHeader(position))
            showHeader(isFirstInHeader(position))
            contact.photoUri?.let { setImageUri(Uri.parse(it)) }
            setOnClickListener { _onContactItemClickListener?.invoke(contact) }
            setOnLongClickListener {
                _onContactItemLongClickListener?.invoke(contact)
                true
            }
            setFadeUpAnimation(this)
        }
    }

    private fun isFirstInHeader(position: Int): Boolean {
        var total = 0
        headersCounts.forEach { count ->
            when {
                position == total -> return true
                else -> total += count
            }
        }
        return false
    }

    private fun getHeader(position: Int): String {
        var total = 0
        headersCounts.withIndex().forEach { (index, headerCount) ->
            when {
                position <= total -> return headers.get(index)
                else -> total += headerCount
            }
        }
        return ""
    }

    fun updateContacts(newContacts: Array<Contact>) {
        _contacts = newContacts
        notifyDataSetChanged()
    }

    fun setOnContactItemClick(onContactItemClickListener: (Contact) -> Unit) {
        _onContactItemClickListener = onContactItemClickListener
    }

    fun setOnContactItemLongClickListener(onContactItemLongClickListener: (Contact) -> Unit) {
        _onContactItemLongClickListener = onContactItemLongClickListener
    }
}