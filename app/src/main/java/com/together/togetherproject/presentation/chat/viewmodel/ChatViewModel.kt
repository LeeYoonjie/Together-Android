package com.together.togetherproject.presentation.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.together.togetherproject.data.model.ChatMessage

class ChatViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> get() = _messages

    fun loadMessages(chatRoomId: String) {
        firestore.collection("chatRooms").document(chatRoomId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error fetching messages: ${error.message}")
                    return@addSnapshotListener
                }
                val messageList = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                _messages.value = messageList
            }
    }

    fun sendMessage(chatRoomId: String, text: String, senderId: String, recipientId: String) {
        val messageId = firestore.collection("chatRooms").document(chatRoomId)
            .collection("messages").document().id
        val message = ChatMessage(
            id = messageId,
            text = text,
            senderId = senderId,
            recipientId = recipientId,
            timestamp = System.currentTimeMillis()
        )
        firestore.collection("chatRooms").document(chatRoomId).collection("messages")
            .document(messageId).set(message)
    }
}