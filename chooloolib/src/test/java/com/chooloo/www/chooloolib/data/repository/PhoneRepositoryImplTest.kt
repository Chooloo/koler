package com.chooloo.www.chooloolib.data.repository

import android.content.ContentResolver
import android.content.ContextWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Test

class PhoneRepositoryImplTest {
    private val repository = PhoneRepositoryImpl(
        ContextWrapper(null),
        object : ContentResolver(null) {},
        Dispatchers.Unconfined,
    )

    @Test
    fun `failed lookup returns no caller`() = runTest {
        assertNull(repository.lookup("111"))
    }
}
