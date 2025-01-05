package com.together.togetherproject.presentation.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.together.togetherproject.data.model.ChatRoom

class ChatRoomViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _chatRoomsLiveData = MutableLiveData<List<ChatRoom>>()
    val chatRoomsLiveData: LiveData<List<ChatRoom>> get() = _chatRoomsLiveData

    fun loadChatRooms() {
        firestore.collection("chatRooms")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error fetching chat rooms: ${error.message}")
                    return@addSnapshotListener
                }

                val chatRooms = snapshot?.toObjects(ChatRoom::class.java) ?: emptyList()
                _chatRoomsLiveData.value = chatRooms
            }
    }
}