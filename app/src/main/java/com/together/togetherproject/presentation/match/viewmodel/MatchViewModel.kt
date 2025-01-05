package com.together.togetherproject.presentation.match.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.together.togetherproject.data.model.MatchedItem

class MatchViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    fun saveMatchedInfoToChatRoom(
        chatRoomId: String,
        matchedItem: MatchedItem,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val matchedInfoRef = firestore.collection("chatRooms")
            .document(chatRoomId)
            .collection("matchedInfo")
            .document()

        matchedInfoRef.set(matchedItem)
            .addOnSuccessListener {
                println("Matched info saved with ID: ${matchedInfoRef.id}")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                println("Error saving matched info: ${exception.message}")
                onFailure(exception)
            }
    }
}