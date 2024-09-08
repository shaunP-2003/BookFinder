package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BooksManageAdapter(
    private var books: List<BookDBEntity>,
    private val onClick: (BookDBEntity) -> Unit,
    private val onDelete: (BookDBEntity) -> Unit
) : RecyclerView.Adapter<BooksManageAdapter.BookViewHolder>() {
    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        private val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        private val bookImageView: ImageView = view.findViewById(R.id.bookImageView)

       //Bind a single book entity to the view holder, setting up text views and the image view.
        fun bind(book: BookDBEntity) {
            titleTextView.text = book.title
            authorTextView.text = book.authors
            itemView.setOnClickListener { onClick(book) }

            Glide.with(itemView.context)
                .load(book.imageUrl)
                .placeholder(R.drawable.image)
                .into(bookImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item_manage, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size
}