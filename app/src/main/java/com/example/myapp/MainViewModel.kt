package com.example.myapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapp.network.Post

class MainViewModel : ViewModel() {
    val posts: MutableLiveData<MutableList<Post>> = MutableLiveData(mutableListOf())
}