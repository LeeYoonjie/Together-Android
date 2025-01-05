package com.together.togetherproject.presentation.chat.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.together.togetherproject.presentation.chat.adapter.ChatAdapter
import com.together.togetherproject.presentation.chat.viewmodel.ChatViewModel
import com.together.togetherproject.presentation.match.view.MatchActivity
import com.together.togetherproject.presentation.match.view.MatchConfirmActivity
import com.together.togetherproject.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val chatViewModel: ChatViewModel by viewModels()
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatRoomId = intent.getStringExtra("CHAT_ROOM_ID") ?: ""
        val recipientId = intent.getStringExtra("AUTHOR_ID") ?: ""

        setupRecyclerView(chatRoomId)
        setupButtons(chatRoomId, recipientId)
        observeMessages(chatRoomId)
    }

    private fun setupRecyclerView(chatRoomId: String) {
        val chatAdapter = ChatAdapter(mutableListOf(), currentUserId)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.chatRecyclerView.adapter = chatAdapter

        chatViewModel.loadMessages(chatRoomId)
        chatViewModel.messages.observe(this) { messages ->
            chatAdapter.updateMessages(messages)
            binding.chatRecyclerView.scrollToPosition(messages.size - 1) // 항상 최신 메시지로 스크롤
        }
    }

    private fun setupButtons(chatRoomId: String, recipientId: String) {
        binding.btnSendMessage.setOnClickListener {
            val messageText = binding.messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                chatViewModel.sendMessage(chatRoomId, messageText, currentUserId, recipientId)
                binding.messageEditText.text.clear()
            } else {
                Toast.makeText(this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelMatch.setOnClickListener {
            cancelMatch(chatRoomId)
        }

        binding.btnStartMatch.setOnClickListener {
            completeMatch()
        }

        binding.btnRegisterInfo.setOnClickListener {
            navigateToRegisterInfo(chatRoomId)
        }

        binding.btnViewInfo.setOnClickListener {
            navigateToViewInfo()
        }
    }

    private fun observeMessages(chatRoomId: String) {
        chatViewModel.messages.observe(this) { messages ->
            val adapter = binding.chatRecyclerView.adapter as ChatAdapter
            adapter.updateMessages(messages)
            binding.chatRecyclerView.scrollToPosition(messages.size - 1)
        }
    }

    private fun cancelMatch(chatRoomId: String) {
        firestore.collection("chatRooms").document(chatRoomId).delete().addOnSuccessListener {
            Toast.makeText(this, "매칭이 취소되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "매칭 취소에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun completeMatch() {
        Toast.makeText(this, "매칭이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun navigateToRegisterInfo(chatRoomId: String) {
        val intent = Intent(this, MatchActivity::class.java).apply {
            putExtra("CHAT_ROOM_ID", chatRoomId)
        }
        startActivity(intent)
    }

    private fun navigateToViewInfo() {
        val intent = Intent(this, MatchConfirmActivity::class.java).apply {
            putExtra("MODE", "VIEW_INFO")
        }
        startActivity(intent)
    }
}