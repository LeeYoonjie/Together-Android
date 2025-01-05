package com.together.togetherproject.presentation.post.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.together.togetherproject.databinding.ActivityRealtimePostBinding
import com.together.togetherproject.presentation.post.adapter.RealTimePostAdapter
import com.together.togetherproject.presentation.post.viewmodel.RealTimePostViewModel
import androidx.lifecycle.ViewModelProvider

class RealTimePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRealtimePostBinding
    private lateinit var viewModel: RealTimePostViewModel
    private lateinit var adapter: RealTimePostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealtimePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RealTimePostViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
        setupSwipeRefresh()

        binding.ivBack.setOnClickListener {
            finish() // 뒤로가기
        }

        binding.fabWrite.setOnClickListener {
            startActivity(Intent(this, PostWriteActivity::class.java)) // 글쓰기 화면으로 이동
        }
    }

    private fun setupRecyclerView() {
        adapter = RealTimePostAdapter(emptyList()) { post ->
            // 아이템 클릭 시 PostDetailActivity로 이동
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("POST_ID", post.id) // 게시물 ID 전달
            startActivity(intent)
        }
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.realTimePosts.observe(this) { posts ->
            adapter.updatePosts(posts)
        }

        viewModel.loadRealTimePosts()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadRealTimePosts() // 새로고침 시 게시물 다시 로드
            binding.swipeRefreshLayout.isRefreshing = false // 새로고침 애니메이션 종료
        }
    }
}