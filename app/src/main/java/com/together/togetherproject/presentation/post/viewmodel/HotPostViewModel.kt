package com.together.togetherproject.presentation.post.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.together.togetherproject.data.model.Post

class HotPostViewModel : ViewModel() {

    private val _hotPosts = MutableLiveData<List<Post>>()
    val hotPosts: LiveData<List<Post>> get() = _hotPosts

    private val firestore = FirebaseFirestore.getInstance()

    fun loadHotPosts() {
        firestore.collection("Post")
            .orderBy("views", Query.Direction.DESCENDING) // 조회수 기준 내림차순 정렬
            .limit(10)
            .get()
            .addOnSuccessListener { result ->
                val posts = result.documents.mapNotNull { it.toObject(Post::class.java) }
                _hotPosts.value = posts
            }
            .addOnFailureListener {
                _hotPosts.value = emptyList()
            }
    }
}