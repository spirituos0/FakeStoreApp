package com.example.myapp.network

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    var isHighlighted: Boolean = false, // Track highlight state
    var highlightRange: IntRange? = null
)
