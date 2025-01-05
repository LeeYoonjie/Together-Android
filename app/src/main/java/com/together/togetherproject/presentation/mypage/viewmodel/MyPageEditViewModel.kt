package com.together.togetherproject.presentation.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class MyPageEditViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _birthdate = MutableLiveData<String>()
    val birthdate: LiveData<String> get() = _birthdate

    private val _userPassword = MutableLiveData<String>()
    val userPassword: LiveData<String> get() = _userPassword

    private val _profileImageUrl = MutableLiveData<String>()
    val profileImageUrl: LiveData<String> get() = _profileImageUrl

    private val _updateStatus = MutableLiveData<String>()
    val updateStatus: LiveData<String> get() = _updateStatus

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        _userName.value = document.getString("name") ?: "Unknown"
                        _userEmail.value = document.getString("email") ?: "No Email"
                        _birthdate.value = document.getString("birthdate") ?: "Unknown"
                        _userPassword.value = document.getString("password") ?: "********"
                        _profileImageUrl.value = document.getString("profileImageUrl")
                            ?: "https://example.com/default-profile.jpg"
                    } else {
                        Log.e("Firestore", "Document does not exist")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error loading user data", e)
                }
        }
    }

    fun updateUserData(name: String, email: String, birthdate: String, password: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val updatedData = mapOf(
                "name" to name,
                "email" to email,
                "birthdate" to birthdate,
                "password" to password,
                "profileImageUrl" to (_profileImageUrl.value ?: "https://example.com/default-profile.jpg")
            )

            firestore.collection("users").document(userId).update(updatedData)
                .addOnSuccessListener {
                    _updateStatus.value = "Profile updated successfully"
                    Log.d("Firestore", "User profile updated")
                }
                .addOnFailureListener { e ->
                    _updateStatus.value = "Failed to update profile"
                    Log.e("Firestore", "Error updating profile", e)
                }
        }
    }

    fun setUserName(value: String) {
        _userName.value = value
    }

    fun setUserEmail(value: String) {
        _userEmail.value = value
    }

    fun setBirthdate(value: String) {
        _birthdate.value = value
    }
}