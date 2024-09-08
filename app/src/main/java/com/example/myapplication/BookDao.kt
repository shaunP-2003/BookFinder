package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface BookDao {
    @Insert
    fun insertBook(book: BookDBEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookDBEntity>

    @Delete
    fun deleteBook(book: BookDBEntity)
}