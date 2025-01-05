package com.together.togetherproject.presentation.post.view

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.together.togetherproject.R
import com.together.togetherproject.databinding.ActivityPostDetailBinding
import com.together.togetherproject.presentation.chat.view.ChatActivity
import com.together.togetherproject.presentation.post.adapter.CommentAdapter
import com.together.togetherproject.presentation.post.viewmodel.PostDetailViewModel
import com.together.togetherproject.data.model.Comment

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private val viewModel: PostDetailViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()

        val postId = intent.getStringExtra("POST_ID") ?: ""
        if (postId.isNotEmpty()) {
            viewModel.loadPost(postId)
            viewModel.loadComments(postId)
        } else {
            Toast.makeText(this, "게시물 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        observeViewModel()

        binding.commentInputSection.findViewById<ImageButton>(R.id.btnSendComment).setOnClickListener {
            val commentText = binding.commentInputSection.findViewById<EditText>(R.id.etComment).text.toString()
            if (commentText.isNotBlank()) {
                viewModel.addComment(postId, commentText)
                binding.commentInputSection.findViewById<EditText>(R.id.etComment).text.clear()
            } else {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter(
            onChatClick = { comment ->
                startChatWithUser(comment.authorId)
            },
            onDeleteClick = { comment ->
                viewModel.deleteComment(comment.id)
                Toast.makeText(this, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(this@PostDetailActivity)
            adapter = commentAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.post.observe(this) { post ->
            binding.post = post
        }

        viewModel.comments.observe(this) { comments ->
            commentAdapter.submitList(comments)
        }
    }

    private fun startChatWithUser(authorId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.post.value?.let { post ->
            val chatRoomId = if (currentUserId < authorId) {
                "$currentUserId-$authorId"
            } else {
                "$authorId-$currentUserId"
            }

            viewModel.createChatRoom(chatRoomId, authorId)

            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("CHAT_ROOM_ID", chatRoomId)
                putExtra("AUTHOR_ID", authorId)
                putExtra("AUTHOR_NAME", post.authorName)
                putExtra("START_ADDRESS", post.startAddress)
                putExtra("DESTINATION", post.destination)
            }
            startActivity(intent)
        }
    }
}