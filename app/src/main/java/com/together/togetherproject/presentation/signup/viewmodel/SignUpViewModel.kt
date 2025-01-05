package com.together.togetherproject.presentation.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class SignUpViewModel : ViewModel() {

    private val _signUpStatus = MutableLiveData<Result<Unit>>() // 회원가입 결과 상태를 LiveData로 관리
    val signUpStatus: LiveData<Result<Unit>> = _signUpStatus

    private val auth: FirebaseAuth = FirebaseAuth.getInstance() // Firebase 인증 인스턴스
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance() // Firestore 인스턴스

    fun signUp(name: String, email: String, password: String, passwordConfirm: String, birthdate: String) {
        // 이메일 형식 검증
        if (!validateEmail(email)) {
            _signUpStatus.value = Result.failure(Exception("이메일 형식이 다릅니다."))
            return
        }
        // 비밀번호 일치 여부 확인
        if (password != passwordConfirm) {
            _signUpStatus.value = Result.failure(Exception("비밀번호가 다릅니다."))
            return
        }
        // 생년월일 형식 검증
        if (!validateBirthdate(birthdate)) {
            _signUpStatus.value = Result.failure(Exception("생년월일 형식이 다릅니다."))
            return
        }

        // Firebase 회원가입 요청
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserInfo(name, email, birthdate)
                } else {
                    _signUpStatus.value = Result.failure(task.exception ?: Exception("알 수 없는 오류"))
                }
            }
    }

    private fun saveUserInfo(name: String, email: String, birthdate: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userData = mapOf(
                "name" to name,
                "email" to email,
                "birthdate" to birthdate,
                "profileImageUrl" to "https://example.com/default-profile.jpg"
            )

            firestore.collection("users").document(userId).set(userData)
                .addOnSuccessListener {
                    _signUpStatus.value = Result.success(Unit)
                    Log.d("Firestore", "User profile saved successfully")
                }
                .addOnFailureListener { e ->
                    _signUpStatus.value = Result.failure(e)
                    Log.e("Firestore", "Error saving profile", e)
                }
        } else {
            _signUpStatus.value = Result.failure(Exception("Failed to get user ID"))
        }
    }

    // 이메일 형식 확인 함수
    private fun validateEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    // 생년월일 형식 확인 함수
    private fun validateBirthdate(birthdate: String) = birthdate.matches(Regex("\\d{8}"))
}