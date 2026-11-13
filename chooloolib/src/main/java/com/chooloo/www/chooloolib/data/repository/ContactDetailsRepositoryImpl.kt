package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Organization
import android.provider.ContactsContract.Data
import com.chooloo.www.chooloolib.data.source.ContentQuery
import com.chooloo.www.chooloolib.data.source.observeAll
import com.chooloo.www.chooloolib.data.source.string
import com.chooloo.www.chooloolib.di.IoDispatcher
import com.chooloo.www.chooloolib.domain.model.ContactDetails
import com.chooloo.www.chooloolib.domain.repository.ContactDetailsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@Singleton
class ContactDetailsRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ContactDetailsRepository {
    override fun details(contactId: Long): Flow<ContactDetails> =
        contentResolver.observeAll(
            ContentQuery(
                uri = Data.CONTENT_URI,
                projection = arrayOf(Data.MIMETYPE, Data.DATA1),
                selection = "${Data.CONTACT_ID} = ? AND ${Data.MIMETYPE} IN (?, ?, ?)",
                selectionArgs = arrayOf(contactId.toString(), Email.CONTENT_ITEM_TYPE, Organization.CONTENT_ITEM_TYPE, WHATSAPP_MIME_TYPE),
            )
        ) { cursor -> cursor.string(Data.MIMETYPE) to cursor.string(Data.DATA1)?.takeIf(String::isNotBlank) }
            .map(::detailsOf)
            .flowOn(ioDispatcher)

    private fun detailsOf(rows: List<Pair<String?, String?>>): ContactDetails {
        fun dataOf(mimeType: String) = rows.filter { it.first == mimeType }.mapNotNull { it.second }
        return ContactDetails(
            emails = dataOf(Email.CONTENT_ITEM_TYPE).distinct(),
            organization = dataOf(Organization.CONTENT_ITEM_TYPE).firstOrNull(),
            whatsappNumber = dataOf(WHATSAPP_MIME_TYPE).firstOrNull()?.let(::whatsappNumberOf),
        )
    }

    companion object {
        private const val WHATSAPP_MIME_TYPE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"

        // WhatsApp stores its contact row as "<digits>@s.whatsapp.net".
        fun whatsappNumberOf(data: String): String? =
            data.substringBefore('@').filter(Char::isDigit).takeIf(String::isNotEmpty)
    }
}
