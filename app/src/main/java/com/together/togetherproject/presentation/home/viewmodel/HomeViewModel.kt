package com.together.togetherproject.presentation.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.together.togetherproject.data.model.Post

class HomeViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _hotPosts = MutableLiveData<List<Post>>()
    val hotPosts: LiveData<List<Post>> get() = _hotPosts

    private val _realTimePosts = MutableLiveData<List<Post>>()
    val realTimePosts: LiveData<List<Post>> get() = _realTimePosts

    fun loadHotPosts() {
        firestore.collection("Post")
            .orderBy("views", Query.Direction.DESCENDING)
            .limit(2)
            .get()
            .addOnSuccessListener { result ->
                _hotPosts.value = result.documents.mapNotNull { it.toObject(Post::class.java) }
            }
            .addOnFailureListener {
                _hotPosts.value = emptyList()
            }
    }

    fun loadRealTimePosts() {
        firestore.collection("Post")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(2)
            .get()
            .addOnSuccessListener { result ->
                _realTimePosts.value = result.documents.mapNotNull { it.toObject(Post::class.java) }
            }
            .addOnFailureListener {
                _realTimePosts.value = emptyList()
            }
    }
}