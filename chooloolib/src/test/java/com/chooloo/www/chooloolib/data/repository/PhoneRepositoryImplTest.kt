package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.content.ContextWrapper
import com.chooloo.www.chooloolib.data.source.ContentSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Test

class PhoneRepositoryImplTest {
    private val repository = PhoneRepositoryImpl(
        ContextWrapper(null),
        ContentSource(object : ContentResolver(null) {}),
        Dispatchers.Unconfined,
    )

    @Test
    fun `failed lookup returns no caller`() = runTest {
        assertNull(repository.lookup("111"))
    }
}
