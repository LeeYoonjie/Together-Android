package com.together.togetherproject.presentation.mypage.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.together.togetherproject.databinding.FragmentMypageBinding
import com.together.togetherproject.presentation.mypage.viewmodel.MyPageViewModel
import com.together.togetherproject.presentation.signin.SignInActivity

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프로필 수정 버튼 클릭 시 MyPageEditActivity로 이동
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), MyPageEditActivity::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼 클릭 시 로그아웃 처리
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        // SwipeRefreshLayout 새로고침 동작 설정
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData() // 데이터 새로 고침 로직 호출
            binding.swipeRefreshLayout.isRefreshing = false // 새로고침 애니메이션 종료
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}