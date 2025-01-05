package com.together.togetherproject.presentation.match.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.together.togetherproject.databinding.ActivityMatchFormConfirmationBinding
import com.together.togetherproject.presentation.match.viewmodel.MatchConfirmViewModel

class MatchConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchFormConfirmationBinding
    private val viewModel: MatchConfirmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchFormConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel과 데이터 바인딩 연결
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // 데이터 설정
        viewModel.setData(
            startAddress = "기흥역 1번 출구",
            destination = "강남대학교 살롬관",
            startTime = "12:00",
            accountNumber = "123412341234",
            paymentAmount = "6000",
            individualPayment = "3000"
        )

        // 계좌번호 복사 버튼 클릭 이벤트
        binding.btnCopyAccount.setOnClickListener {
            copyAccountNumberToClipboard()
        }

        // 뒤로가기 버튼 클릭 이벤트
        binding.btnBack.setOnClickListener {
            onBackPressed() // Activity 종료
        }
    }

    private fun copyAccountNumberToClipboard() {
        val accountNumber = binding.tvAccountNumber.text.toString()
        if (accountNumber.isNotEmpty()) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("계좌번호", accountNumber)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "계좌번호가 복사되었습니다: $accountNumber", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "복사할 계좌번호가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}