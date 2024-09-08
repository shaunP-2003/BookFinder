package com.example.myapplication

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BooksApiServiceTest {

    private val service = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BooksApiService::class.java)

    @Test
    fun testAPISuccessResponse() = runBlocking {
        val response = service.searchBooks("android", "AIzaSyBO7F_CBwyWvcGJOZRZnaUsc_QDTCyA8b0")
        assertTrue("Check that the response is successful", response.isSuccessful)
        assertNotNull("Check that the response body is not null", response.body())
        assertTrue("Check that the list of books is not empty", response.body()?.items?.isNotEmpty() == true)
    }
}