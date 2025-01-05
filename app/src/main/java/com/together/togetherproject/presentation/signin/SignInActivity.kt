package com.together.togetherproject.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.together.togetherproject.R
import com.together.togetherproject.databinding.ActivitySignInBinding
import com.together.togetherproject.MainActivity
import com.together.togetherproject.presentation.signup.view.SignUpActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBinding 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        // Firebase Auth 초기화
        auth = FirebaseAuth.getInstance()

        // 로그인 버튼 클릭 이벤트
        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()

            // 로그인 함수 호출
            signIn(email, password)
        }

        // 회원가입 버튼 클릭 이벤트
        binding.buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("이메일과 비밀번호를 입력하세요.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공 시 MainActivity로 이동
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // 로그인 실패 시 오류 메시지 표시
                    showError("로그인에 실패했습니다. 이메일과 비밀번호를 확인하세요.")
                }
            }
    }

    private fun showError(message: String) {
        binding.textViewErrorMessage.text = message
        binding.textViewErrorMessage.visibility = View.VISIBLE
    }
}