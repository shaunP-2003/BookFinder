package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BookshelfActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var booksManageAdapter: BooksManageAdapter
    private lateinit var emptyView: TextView
    private var books: MutableList<BookDBEntity> = mutableListOf()
    private val startBookDetailActivityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Refresh the book list if the book details activity completes successfully
            fetchBooksFromDatabase()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookshelf)
        recyclerView = findViewById(R.id.booksRecyclerView)
        emptyView = findViewById(R.id.emptyView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter setup with click listeners for viewing and deleting books
        booksManageAdapter = BooksManageAdapter(books, { bookEntity ->
            val intent = Intent(this, BookshelfDetailsActivity::class.java)
            intent.putExtra("bookEntityData", Gson().toJson(bookEntity))
            startBookDetailActivityForResult.launch(intent)
        }, { bookEntity ->
            deleteBook(bookEntity)
        })
        recyclerView.adapter = booksManageAdapter
        fetchBooksFromDatabase()
    }

    private fun fetchBooksFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedBooks = AppDatabase.getDatabase(applicationContext).bookDao().getAllBooks()
            books.clear()
            books.addAll(fetchedBooks)
            withContext(Dispatchers.Main) {
                updateUI()
            }
        }
    }

    private fun updateUI() {
        if (books.isEmpty()) {
            emptyView.visibility = TextView.VISIBLE
            recyclerView.visibility = RecyclerView.GONE
        } else {
            emptyView.visibility = TextView.GONE
            recyclerView.visibility = RecyclerView.VISIBLE
        }
        booksManageAdapter.notifyDataSetChanged()
    }

    private fun deleteBook(book: BookDBEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(applicationContext).bookDao().deleteBook(book)
            books.remove(book)
            withContext(Dispatchers.Main) {
                booksManageAdapter.notifyDataSetChanged()
                updateUI()
            }
        }
    }
}