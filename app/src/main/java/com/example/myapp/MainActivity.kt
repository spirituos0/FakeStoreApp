package com.example.myapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.network.ApiClient
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var adapter: PostAdapter
    private var fontSize: Float = 16f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: MainViewModel by viewModels()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PostAdapter(mutableListOf(), fontSize)
        recyclerView.adapter = adapter

        viewModel.posts.observe(this) { posts ->
            adapter.updatePosts(posts)
        }



        // Initialize SeekBar
        val fontSizeSeekBar: SeekBar = findViewById(R.id.fontSizeSeekBar)
        fontSizeSeekBar.progress = fontSize.toInt()

        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                fontSize = progress.toFloat()
                adapter.updateFontSize(fontSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Load posts if not already loaded
        if (viewModel.posts.value.isNullOrEmpty()) {
            loadPosts(viewModel)
        }

        // Handle context menu actions
        registerForContextMenu(recyclerView)
    }

    private fun loadPosts(viewModel: MainViewModel) {
        lifecycleScope.launch {
            try {
                val posts = ApiClient.apiService.getPosts()
                viewModel.posts.value = posts.toMutableList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        adapter.onMenuItemClick(item, this)
        return super.onContextItemSelected(item)
    }
}
