package com.chooloo.www.chooloolib.domain.repository.contact

import android.Manifest.permission.WRITE_CONTACTS
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_EDIT
import android.content.Intent.ACTION_INSERT
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import androidx.annotation.RequiresPermission
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.domain.model.record.ContactRecord
import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import com.chooloo.www.chooloolib.domain.repository.blocked.BlockedRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.utils.annotation.RequiresDefaultDialer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val phones: PhoneRepository,
    private val blocked: BlockedRepository,
    @ApplicationContext private val context: Context,
    private val contentResolverFactory: ContentResolverFactory
) : BaseRepositoryImpl(), ContactRepository {
    override suspend fun getContact(contactId: Long): ContactRecord? =
        contentResolverFactory.getContactsContentResolver(contactId = contactId).getItems()
            .getOrNull(0)

    override fun getContactFlow(contactId: Long): Flow<ContactRecord?> = flow {
        contentResolverFactory.getContactsContentResolver(contactId = contactId).getItemsFlow()
            .collect {
                emit(it.getOrNull(0))
            }
    }

    override suspend fun getContacts(filter: String?): List<ContactRecord> =
        contentResolverFactory.getContactsContentResolver(filter = filter).getItems()

    override fun getContactsFlow(filter: String?): Flow<List<ContactRecord>> =
        contentResolverFactory.getContactsContentResolver(filter = filter).getItemsFlow()

    override suspend fun createContact(number: String) {
        context.startActivity(
            Intent(ACTION_INSERT)
                .setType(ContactsContract.Contacts.CONTENT_TYPE)
                .putExtra(ContactsContract.Intents.Insert.PHONE, number)
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun viewContact(contactId: Long) {
        context.startActivity(
            Intent(ACTION_VIEW)
                .setData(
                    Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI,
                        contactId.toString()
                    )
                )
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override suspend fun editContact(contactId: Long) {
        context.startActivity(
            Intent(
                ACTION_EDIT,
                ContactsContract.Contacts.CONTENT_URI
            )
                .setData(
                    ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI,
                        contactId
                    )
                )
                .addFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    @RequiresDefaultDialer
    override suspend fun blockContact(contactId: Long) {
        val contactAccounts = phones.getContactAccounts(contactId)
        contactAccounts.forEach { blocked.blockNumber(it.number) }
    }

    @RequiresPermission(WRITE_CONTACTS)
    override suspend fun deleteContact(contactId: Long) {
        context.contentResolver.delete(
            Uri.withAppendedPath(
                Contacts.CONTENT_URI, contactId.toString()
            ), null, null
        )
    }

    override suspend fun unblockContact(contactId: Long) {
        val contactAccounts = phones.getContactAccounts(contactId)
        contactAccounts.forEach { blocked.unblockNumber(it.number) }
    }

    override suspend fun getIsContactBlocked(contactId: Long): Boolean {
        val contactAccounts = phones.getContactAccounts(contactId)
        return contactAccounts.all { blocked.isNumberBlocked(it.number) }
    }

    @RequiresPermission(WRITE_CONTACTS)
    override suspend fun toggleContactFavorite(contactId: Long, isFavorite: Boolean) {
        val contentValues = ContentValues()
        contentValues.put(Contacts.STARRED, if (isFavorite) 1 else 0)
        val filter = "${Contacts._ID}=$contactId"
        context.contentResolver.update(Contacts.CONTENT_URI, contentValues, filter, null)
    }
}