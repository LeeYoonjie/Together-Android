package com.together.togetherproject.presentation.chat.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.together.togetherproject.databinding.ActivityChatRoomListBinding
import com.together.togetherproject.data.model.ChatRoom
import com.together.togetherproject.presentation.chat.adapter.ChatRoomAdapter
import com.together.togetherproject.presentation.chat.viewmodel.ChatRoomViewModel

class ChatRoomListActivity : AppCompatActivity() {

    private val chatRoomViewModel: ChatRoomViewModel by viewModels()
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var binding: ActivityChatRoomListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBackButton()

        observeChatRooms()
        chatRoomViewModel.loadChatRooms()
    }

    private fun setupRecyclerView() {
        binding.chatRoomRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRoomAdapter = ChatRoomAdapter(emptyList()) { chatRoom ->
            openChatRoom(chatRoom)
        }
        binding.chatRoomRecyclerView.adapter = chatRoomAdapter
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            onBackPressed() // 현재 Activity 종료
        }
    }

    private fun observeChatRooms() {
        chatRoomViewModel.chatRoomsLiveData.observe(this) { chatRooms ->
            chatRoomAdapter.updateData(chatRooms)
        }
    }

    private fun openChatRoom(chatRoom: ChatRoom) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("CHAT_ROOM_ID", chatRoom.roomId)
            putExtra("PARTICIPANTS", chatRoom.participants.toTypedArray())
        }
        startActivity(intent)
    }
}