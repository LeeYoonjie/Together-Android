package com.together.togetherproject.presentation.post.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.together.togetherproject.data.model.ChatRoom
import com.together.togetherproject.data.model.Comment
import com.together.togetherproject.data.model.Post

class PostDetailViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> get() = _post

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    fun loadPost(postId: String) {
        firestore.collection("Post").document(postId).get()
            .addOnSuccessListener { document ->
                val post = document.toObject(Post::class.java)
                if (post != null) {
                    firestore.collection("users").document(post.authorId).get()
                        .addOnSuccessListener { userDoc ->
                            val authorName = userDoc.getString("name") ?: "Unknown"
                            val updatedPost = post.copy(authorName = authorName)
                            _post.value = updatedPost
                        }
                        .addOnFailureListener {
                            println("Error loading author name: ${it.message}")
                            _post.value = post!! // 이름을 불러오지 못해도 게시물은 표시
                        }
                } else {
                    println("Post not found or invalid structure.")
                }
            }
            .addOnFailureListener {
                println("Error loading post: ${it.message}")
            }
    }

    fun loadComments(postId: String) {
        firestore.collection("comments")
            .whereEqualTo("postId", postId)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    println("Error loading comments: ${exception.message}")
                    return@addSnapshotListener
                }

                val comments = querySnapshot?.toObjects(Comment::class.java) ?: emptyList()
                updateCommentAuthors(comments)
            }
    }

    private fun updateCommentAuthors(comments: List<Comment>) {
        val updatedComments = mutableListOf<Comment>()
        comments.forEach { comment ->
            firestore.collection("users").document(comment.authorId).get()
                .addOnSuccessListener { document ->
                    val authorName = document.getString("name") ?: "Unknown"
                    updatedComments.add(comment.copy(authorNickname = authorName))

                    if (updatedComments.size == comments.size) {
                        _comments.value = updatedComments.sortedBy { it.timestamp }
                    }
                }
                .addOnFailureListener {
                    updatedComments.add(comment.copy(authorNickname = "Unknown"))

                    if (updatedComments.size == comments.size) {
                        _comments.value = updatedComments.sortedBy { it.timestamp }
                    }
                }
        }
    }

    fun addComment(postId: String, content: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newComment = Comment(
            postId = postId,
            content = content,
            authorId = currentUserId,
            timestamp = System.currentTimeMillis()
        )
        firestore.collection("comments").add(newComment)
    }

    fun deleteComment(commentId: String) {
        firestore.collection("comments").document(commentId).delete()
    }

    fun createChatRoom(chatRoomId: String, recipientId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val chatRoom = ChatRoom(
            roomId = chatRoomId,
            participants = listOf(currentUserId, recipientId),
            lastMessage = "",
            timestamp = System.currentTimeMillis()
        )

        firestore.collection("chatRooms").document(chatRoomId).set(chatRoom)
            .addOnSuccessListener {
                println("Chat room created successfully.")
            }
            .addOnFailureListener {
                println("Error creating chat room: ${it.message}")
            }
    }
}