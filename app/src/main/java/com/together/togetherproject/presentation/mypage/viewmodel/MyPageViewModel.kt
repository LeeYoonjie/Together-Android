package com.together.togetherproject.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class MyPageViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        loadUserData()
    }

    fun loadUserData() {
        _isLoading.value = true
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    _isLoading.value = false
                    if (document.exists()) {
                        _userName.value = document.getString("name") ?: "Unknown"
                        _userEmail.value = document.getString("email") ?: "No Email"
                    } else {
                        Log.e("Firestore", "Document does not exist")
                        _errorMessage.value = "User data not found."
                        _userName.value = "No Name"
                        _userEmail.value = "No Email"
                    }
                }
                .addOnFailureListener { exception ->
                    _isLoading.value = false
                    Log.e("Firestore", "Error fetching user data", exception)
                    _errorMessage.value = "Failed to fetch user data."
                    _userName.value = "Error"
                    _userEmail.value = "Error"
                }
        } else {
            _isLoading.value = false
            _userName.value = "Guest"
            _userEmail.value = "No Email"
        }
    }

    fun refreshData() {
        loadUserData()
    }

    fun logout() {
        auth.signOut()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}