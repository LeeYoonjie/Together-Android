package com.together.togetherproject.presentation.match.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.together.togetherproject.R
import com.together.togetherproject.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        displayHistory()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.toolbar.title = "이용 내역"
    }

    private fun displayHistory() {
        binding.tvStartLocation.text = "출발지: 기흥역 1번 출구"
        binding.tvDestination.text = "도착지: 강남대학교 샬롬관 입구"
        binding.tvStartTime.text = "출발 시간: 12:00"
        binding.tvMatchStatus.text = "매칭 완료"
    }
}