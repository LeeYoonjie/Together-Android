package com.together.togetherproject.presentation.post.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.together.togetherproject.data.model.Post

class RealTimePostViewModel : ViewModel() {

    private val _realTimePosts = MutableLiveData<List<Post>>()
    val realTimePosts: LiveData<List<Post>> get() = _realTimePosts

    private val firestore = FirebaseFirestore.getInstance()

    fun loadRealTimePosts() {
        firestore.collection("Post")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val posts = result.documents.mapNotNull {
                    it.toObject(Post::class.java)?.also { post ->
                        Log.d("RealTimePostViewModel", "Post timestamp: ${post.timestamp}")
                    }
                }
                _realTimePosts.value = posts
            }
            .addOnFailureListener {
                _realTimePosts.value = emptyList()
            }
    }
}