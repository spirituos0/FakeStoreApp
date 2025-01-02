package com.example.myapp

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.network.ApiClient
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var adapter: ProductAdapter
    private var fontSize: Float = 16f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(mutableListOf(), fontSize)
        recyclerView.adapter = adapter

        val fontSizeSeekBar: SeekBar = findViewById(R.id.fontSizeSeekBar)
        fontSizeSeekBar.progress = fontSize.toInt()

        val minFontSize = 12f
        val maxFontSize = 24f
        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                fontSize = progress.toFloat()
                fontSize = minFontSize.coerceAtLeast(fontSize.coerceAtMost(maxFontSize))
                adapter.updateFontSize(fontSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        loadProducts()
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            try {
                val products = ApiClient.apiService.getProducts()
                adapter.updateProducts(products)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

