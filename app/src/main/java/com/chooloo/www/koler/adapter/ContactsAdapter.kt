package com.chooloo.www.koler.adapter

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.entity.ContactsBundle
import com.chooloo.www.koler.ui.widgets.ListItemHolder
import com.chooloo.www.koler.util.setFadeUpAnimation

open class ContactsAdapter : RecyclerView.Adapter<ListItemHolder>() {

    private var _contacts: Array<Contact> = arrayOf()
    private var _onContactItemClickListener: ((Contact) -> Unit?)? = null
    private var _onContactItemLongClickListener: ((Contact) -> Unit)? = null
    private var _headersCounts: Array<Int> = arrayOf()
    private var _headers: Array<String> = arrayOf()

    override fun getItemCount() = _contacts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(parent.context)

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val contact = _contacts[position]
        holder.listItem.apply {
            bigText = contact.name
            headerText = getHeader(position)
            isHeaderVisible = isFirstInHeader(position)
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
        _headersCounts.forEach { count ->
            when (position) {
                total -> return true
                else -> total += count
            }
        }
        return false
    }

    private fun getHeader(position: Int): String {
        var total = 0
        _headersCounts.withIndex().forEach { (index, headerCount) ->
            when {
                position <= total -> return _headers.get(index)
                else -> total += headerCount
            }
        }
        return ""
    }

    fun updateContacts(contactsBundle: ContactsBundle) {
        _contacts = contactsBundle.contacts
        _headers = contactsBundle.headers
        _headersCounts = contactsBundle.headersCounts
        notifyDataSetChanged()
    }

    fun setOnContactItemClick(onContactItemClickListener: (Contact) -> Unit) {
        _onContactItemClickListener = onContactItemClickListener
    }

    fun setOnContactItemLongClickListener(onContactItemLongClickListener: (Contact) -> Unit) {
        _onContactItemLongClickListener = onContactItemLongClickListener
    }
}