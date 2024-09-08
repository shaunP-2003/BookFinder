package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.collections.List

class BooksAdapter(private val books: List<Book>, private val onClick: (Book) -> Unit) :
    RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        private val imageView: ImageView = view.findViewById(R.id.imageView)

        fun bind(book: Book, onClick: (Book) -> Unit) {
            titleTextView.text = book.volumeInfo.title
            authorTextView.text = book.volumeInfo.authors?.joinToString() ?: "Unknown Author"
            book.volumeInfo.imageLinks?.thumbnail?.let { imageUrl ->
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image)
                    .into(imageView)
            }
            itemView.setOnClickListener { onClick(book) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position], onClick)
    }

    override fun getItemCount() = books.size
}