package com.together.togetherproject.presentation.post.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.together.togetherproject.data.model.Post
import android.util.Log

class PostWriteViewModel : ViewModel() {

    private val _postWriteStatus = MutableLiveData<Status>()
    val postWriteStatus: LiveData<Status> get() = _postWriteStatus

    private val firestore = FirebaseFirestore.getInstance()

    fun writePost(
        title: String,
        content: String,
        authorId: String,
        authorName: String,
        startAddress: String,
        destination: String
    ) {
        _postWriteStatus.value = Status.Loading

        val post = Post(
            title = title,
            content = content,
            authorId = authorId,
            authorName = authorName,
            startAddress = startAddress,
            destination = destination,
            timestamp = System.currentTimeMillis()
        )

        firestore.collection("Post")
            .add(post)
            .addOnSuccessListener { documentReference ->
                val postId = documentReference.id
                documentReference.update("id", postId)
                    .addOnSuccessListener {
                        _postWriteStatus.value = Status.Success
                    }
                    .addOnFailureListener { exception ->
                        Log.e("PostWriteViewModel", "Error updating post ID: ${exception.message}")
                        _postWriteStatus.value = Status.Error("Failed to update post ID")
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("PostWriteViewModel", "Error writing post: ${exception.message}", exception)
                _postWriteStatus.value = Status.Error(exception.message ?: "Unknown error")
            }
    }

    sealed class Status {
        object Loading : Status()
        object Success : Status()
        data class Error(val message: String) : Status()
    }
}