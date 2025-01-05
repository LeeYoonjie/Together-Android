package com.together.togetherproject.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.together.togetherproject.R

class NotificationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView

    private val notifications = listOf(
        "알림 1: 새로운 메시지가 도착했습니다.",
        "알림 2: 내 게시물에 댓글이 달렸습니다.",
        "알림 3: 새로운 친구 요청이 있습니다."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewNotifications)
        emptyView = view.findViewById(R.id.tvEmptyNotifications)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = NotificationAdapter(notifications)

        // 알림이 없는 경우 처리
        if (notifications.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }

        return view
    }
}