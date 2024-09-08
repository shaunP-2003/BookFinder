package com.example.myapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var bookDao: BookDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        bookDao = db.bookDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndLoadBookTest() {
        val book = BookDBEntity("id1", "Title", "Author", "Description", "url")
        bookDao.insertBook(book)
        val loaded = bookDao.getAllBooks()
        assertTrue("Book was inserted and loaded", loaded.contains(book))
    }

    @Test
    fun deleteBookTest() {
        val book = BookDBEntity("id2", "Title2", "Author2", "Description2", "url2")
        bookDao.insertBook(book)
        bookDao.deleteBook(book)
        val loaded = bookDao.getAllBooks()
        assertFalse("Book was deleted", loaded.contains(book))
    }
}
