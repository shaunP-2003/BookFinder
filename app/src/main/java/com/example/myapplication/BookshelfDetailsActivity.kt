package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookshelfDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookshelf_details)

        val bookDBEntityJson = intent.getStringExtra("bookEntityData")
        val bookDBEntity = Gson().fromJson(bookDBEntityJson, BookDBEntity::class.java)

        // Populate the views
        findViewById<TextView>(R.id.titleTextView).text = bookDBEntity.title
        findViewById<TextView>(R.id.authorTextView).text = bookDBEntity.authors
        findViewById<TextView>(R.id.descriptionTextView).text = bookDBEntity.description ?: "No Description Available"

        val imageView = findViewById<ImageView>(R.id.imageView)
        Glide.with(this)
            .load(bookDBEntity.imageUrl)
            .placeholder(R.drawable.image) // Replace 'image' with a suitable placeholder name if needed
            .into(imageView)

        // Delete Button
        findViewById<Button>(R.id.deleteButton).setOnClickListener {
            deleteBook(bookDBEntity)
        }
    }

    private fun deleteBook(book: BookDBEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(applicationContext).bookDao().deleteBook(book)
            runOnUiThread {
                setResult(RESULT_OK)
                Toast.makeText(applicationContext, "Book deleted", Toast.LENGTH_SHORT).show()
                finish()  // Close this activity and return
            }
        }
    }
}