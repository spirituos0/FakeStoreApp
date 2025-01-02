package com.example.myapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapp.models.Product

class MainViewModel : ViewModel() {
    val posts: MutableLiveData<MutableList<Product>> = MutableLiveData(mutableListOf())
}