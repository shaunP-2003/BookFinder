package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "books")
data class BookDBEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "authors")
    val authors: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?
)