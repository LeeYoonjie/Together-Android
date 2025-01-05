package com.together.togetherproject.presentation.mypage.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.together.togetherproject.databinding.ActivityMypageEditBinding
import com.together.togetherproject.presentation.mypage.viewmodel.MyPageEditViewModel

class MyPageEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageEditBinding
    private val viewModel: MyPageEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 20) {
                    binding.etName.error = "이름은 20자를 초과할 수 없습니다."
                } else {
                    viewModel.setUserName(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.etEmail.error = "유효한 이메일 주소를 입력하세요."
                } else {
                    viewModel.setUserEmail(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etBirthdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 8) {
                    binding.etBirthdate.error = "생년월일은 8자를 초과할 수 없습니다."
                } else {
                    viewModel.setBirthdate(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val birthdate = binding.etBirthdate.text.toString().trim()
            val password = viewModel.userPassword.value ?: ""

            if (validateInput(name, email, birthdate)) {
                viewModel.updateUserData(name, email, birthdate, password)
                finish() // 데이터 업데이트 후 종료
            }
        }

        viewModel.updateStatus.observe(this) { status ->
            Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(name: String, email: String, birthdate: String): Boolean {
        var isValid = true

        if (name.length > 20) {
            binding.etName.error = "이름은 20자를 초과할 수 없습니다."
            isValid = false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "유효한 이메일 주소를 입력하세요."
            isValid = false
        }

        if (birthdate.length > 8) {
            binding.etBirthdate.error = "생년월일은 8자를 초과할 수 없습니다."
            isValid = false
        }

        return isValid
    }
}