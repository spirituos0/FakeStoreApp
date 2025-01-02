package com.example.myapp.models

data class Product(
    var title: String,
    var price: Double,
    var description: String,
    var category: String,
    var image: String,
    var rating: Rating
)

data class Rating(
    var rate: Double,
    var count: Int
)