package com.example.myapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.network.Post

class PostAdapter(private var posts: MutableList<Post>, private var fontSize: Float) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    companion object {
        private const val MENU_COPY = 1
        private const val MENU_HIGHLIGHT = 2
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val bodyTextView: EditText = itemView.findViewById(R.id.bodyTextView)

        init {
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu,
            v: View,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu.setHeaderTitle("Select Action")
            menu.add(adapterPosition, MENU_COPY, 0, "Copy")
            menu.add(adapterPosition, MENU_HIGHLIGHT, 1, "Highlight")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        println("onCreateViewHolder called for position $viewType")
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        println("onBindViewHolder called for position $position")
        val post = posts[holder.adapterPosition]

        // Set title and body
        holder.titleTextView.text = post.title
        holder.bodyTextView.setText(post.body)

        // Apply font size
        holder.titleTextView.textSize = fontSize

        // Apply bold style to title
        holder.titleTextView.setTypeface(null, android.graphics.Typeface.BOLD)

        // Restore highlight if applicable
        val spannable = SpannableString(post.body)
        post.highlightRange?.let { range ->
            spannable.setSpan(
                BackgroundColorSpan(Color.YELLOW),
                range.first,
                range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        holder.bodyTextView.setText(spannable, TextView.BufferType.SPANNABLE)
        holder.bodyTextView.textSize = fontSize





        // Listen for text changes
        holder.bodyTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    posts[adapterPosition] = posts[adapterPosition].copy(body = s.toString()) // Update the post body
                }
            }
        })

        // Set click listener for context menu
        holder.itemView.setOnLongClickListener {
            holder.itemView.showContextMenu()
            true
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        newPosts.forEach { newPost ->
            val existingPost = posts.find { it.id == newPost.id }
            if (existingPost != null) {
                newPost.isHighlighted = existingPost.isHighlighted
            }
        }
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }

    fun updateFontSize(newFontSize: Float) {
        fontSize = newFontSize
        notifyDataSetChanged()
    }

    fun onMenuItemClick(item: MenuItem, context: Context) {
        val position = item.groupId
        if (position in posts.indices) {
            val post = posts[position]
            when (item.itemId) {
                MENU_COPY -> {
                    copyToClipboard(post.body, context)
                    Toast.makeText(context, "Text copied!", Toast.LENGTH_SHORT).show()
                }

                MENU_HIGHLIGHT -> {
                    highlightText(position)
                    Toast.makeText(context, "Text highlighted!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun copyToClipboard(text: String, context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }

    private fun highlightText(position: Int) {
        val post = posts[position]
        if (post.highlightRange != null) {
            post.highlightRange = null
            post.isHighlighted = false
        } else {
            post.highlightRange = 0 until post.body.length
            post.isHighlighted = true
        }
        notifyItemChanged(position)
    }
}
