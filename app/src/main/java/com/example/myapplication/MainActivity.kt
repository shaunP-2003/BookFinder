package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var booksAdapter: BooksAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup bottom navigation bar with navigation items.
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_main -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.navigation_bookshelf -> {
                    // Intent to open the BookshelfActivity if it's a separate activity
                    startActivity(Intent(this, BookshelfActivity::class.java))
                    true
                }

                else -> false
            }
        }

        // Initializes the RecyclerView with an adapter.
        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.booksRecyclerView)
        booksAdapter = BooksAdapter(listOf()) { book ->
            val intent = Intent(this, BookDetailsActivity::class.java)
            intent.putExtra(
                "bookData",
                Gson().toJson(book)
            )  // Correctly passing the entire Book object
            startActivity(intent)
        }
        recyclerView.adapter = booksAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString()
                if (searchText.length >= 3) {
                    searchBooks(searchText)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Setup the search text input to handle user queries.
    private fun searchBooks(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response =
                    ApiClient.service.searchBooks(query, "AIzaSyBO7F_CBwyWvcGJOZRZnaUsc_QDTCyA8b0")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val books = response.body()!!.items
                        if (books.isNotEmpty()) {
                            booksAdapter = BooksAdapter(books) { book ->
                                val intent =
                                    Intent(this@MainActivity, BookDetailsActivity::class.java)
                                intent.putExtra("bookData", Gson().toJson(book))
                                startActivity(intent)
                            }
                            recyclerView.adapter = booksAdapter
                        } else {
                            Toast.makeText(this@MainActivity, "No results found", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        // Handling cases where the response is not successful
                        handleError(response.code())
                    }
                }
            } catch (e: Exception) {
                // Handling network errors or other exceptions
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun handleError(errorCode: Int) {
        when (errorCode) {
            404 -> Toast.makeText(this, "No entries found", Toast.LENGTH_LONG).show()
            500 -> Toast.makeText(this, "Server error", Toast.LENGTH_LONG).show()
            else -> Toast.makeText(this, "Unexpected error occurred", Toast.LENGTH_LONG).show()
        }
    }
}