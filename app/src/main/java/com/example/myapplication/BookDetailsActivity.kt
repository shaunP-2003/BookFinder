package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BookDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        val bookJson = intent.getStringExtra("bookData")
        val book = Gson().fromJson(bookJson, Book::class.java)

        findViewById<TextView>(R.id.titleTextView).text = book.volumeInfo.title
        findViewById<TextView>(R.id.authorTextView).text = book.volumeInfo.authors?.joinToString() ?: "Unknown Author"
        findViewById<TextView>(R.id.descriptionTextView).text = book.volumeInfo.description ?: "No Description Available"

        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this)
            .load(book.volumeInfo.imageLinks?.thumbnail)
            .placeholder(R.drawable.image)
            .into(imageView)

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val bookDBEntity = BookDBEntity(
                id = book.id,
                title = book.volumeInfo.title,
                authors = book.volumeInfo.authors?.joinToString() ?: "Unknown",
                description = book.volumeInfo.description,
                imageUrl = book.volumeInfo.imageLinks?.thumbnail
            )
            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(this@BookDetailsActivity).bookDao().insertBook(bookDBEntity)
                runOnUiThread {
                    Toast.makeText(applicationContext, "Book Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }
}