package com.together.togetherproject.presentation.post.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.together.togetherproject.databinding.ActivityHotPostBinding
import com.together.togetherproject.presentation.post.adapter.HotPostAdapter
import com.together.togetherproject.presentation.post.viewmodel.HotPostViewModel
import androidx.lifecycle.ViewModelProvider

class HotPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHotPostBinding
    private lateinit var viewModel: HotPostViewModel
    private lateinit var adapter: HotPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[HotPostViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupSwipeRefresh()

        binding.ivBack.setOnClickListener {
            finish() // 뒤로가기 버튼 기능
        }

        binding.fabWrite.setOnClickListener {
            startActivity(Intent(this, PostWriteActivity::class.java)) // 글쓰기 화면 이동
        }
    }

    private fun setupRecyclerView() {
        adapter = HotPostAdapter(emptyList())
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.hotPosts.observe(this) { posts ->
            adapter.updatePosts(posts)
        }

        viewModel.loadHotPosts()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadHotPosts() // 새로고침 시 게시물 다시 로드
            binding.swipeRefreshLayout.isRefreshing = false // 새로고침 애니메이션 종료
        }
    }
}