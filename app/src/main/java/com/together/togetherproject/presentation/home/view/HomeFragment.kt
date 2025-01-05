package com.together.togetherproject.presentation.home.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.together.togetherproject.R
import com.together.togetherproject.presentation.chat.view.ChatActivity
import com.together.togetherproject.presentation.chat.view.ChatRoomListActivity
import com.together.togetherproject.presentation.home.adapter.HomePostAdapter
import com.together.togetherproject.presentation.home.viewmodel.HomeViewModel
import com.together.togetherproject.presentation.match.view.HistoryActivity
import com.together.togetherproject.presentation.post.view.HotPostActivity
import com.together.togetherproject.presentation.post.view.RealTimePostActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var hotPostAdapter: HomePostAdapter
    private lateinit var realTimePostAdapter: HomePostAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        setupRecyclerView(view)
        observeViewModel()
        setupSwipeRefresh()

        // HotPosts More 버튼 클릭 이벤트
        val btnHotPostsMore = view.findViewById<ImageButton>(R.id.btnHotPostsMore)
        btnHotPostsMore.setOnClickListener {
            val intent = Intent(requireContext(), HotPostActivity::class.java)
            startActivity(intent)
        }

        // RealTimePosts More 버튼 클릭 이벤트
        val btnRealTimePostsMore = view.findViewById<ImageButton>(R.id.btnRealTimePostsMore)
        btnRealTimePostsMore.setOnClickListener {
            val intent = Intent(requireContext(), RealTimePostActivity::class.java)
            startActivity(intent)
        }

        val ivNotification = view.findViewById<ImageView>(R.id.ivNotification)
        ivNotification.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            startActivity(intent)
        }

        val ivSettings = view.findViewById<ImageView>(R.id.ivSettings)
        ivSettings.setOnClickListener {
            val intent = Intent(requireContext(), ChatRoomListActivity::class.java)
            startActivity(intent)
        }

        val cardBanner = view.findViewById<CardView>(R.id.cardBanner)
        cardBanner.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun setupRecyclerView(view: View) {
        val hotPostsRecyclerView = view.findViewById<RecyclerView>(R.id.rvHotPosts)
        val realTimePostsRecyclerView = view.findViewById<RecyclerView>(R.id.rvRealTimePosts)

        hotPostAdapter = HomePostAdapter(emptyList())
        realTimePostAdapter = HomePostAdapter(emptyList())

        hotPostsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        realTimePostsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        hotPostsRecyclerView.adapter = hotPostAdapter
        realTimePostsRecyclerView.adapter = realTimePostAdapter
    }

    private fun observeViewModel() {
        homeViewModel.hotPosts.observe(viewLifecycleOwner) { posts ->
            hotPostAdapter.updatePosts(posts.take(2)) // 2개의 게시물만 표시
        }

        homeViewModel.realTimePosts.observe(viewLifecycleOwner) { posts ->
            realTimePostAdapter.updatePosts(posts.take(2)) // 2개의 게시물만 표시
        }

        // ViewModel에서 데이터를 로드
        homeViewModel.loadHotPosts()
        homeViewModel.loadRealTimePosts()
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            // 새 데이터를 가져오기 위해 ViewModel 메서드 호출
            homeViewModel.loadHotPosts()
            homeViewModel.loadRealTimePosts()

            // 새로고침 완료 후 애니메이션 제거
            swipeRefreshLayout.isRefreshing = false
        }
    }
}