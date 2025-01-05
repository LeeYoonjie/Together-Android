package com.together.togetherproject.presentation.post.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.together.togetherproject.databinding.ActivityPostWriteBinding
import com.together.togetherproject.presentation.post.viewmodel.PostWriteViewModel

class PostWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostWriteBinding
    private val viewModel: PostWriteViewModel by viewModels()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCancel.setOnClickListener {
            finish()
        }

        binding.tvSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            val startAddress = binding.etAddress.text.toString()
            val destination = binding.etDestination.text.toString()
            val authorId = FirebaseAuth.getInstance().currentUser?.uid ?: "Unknown User"

            if (title.isNotBlank() && content.isNotBlank() && startAddress.isNotBlank() && destination.isNotBlank()) {
                loadAuthorName(authorId) { authorName ->
                    viewModel.writePost(title, content, authorId, authorName, startAddress, destination)
                }
            } else {
                Toast.makeText(this, "모든 빈칸을 채워주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.postWriteStatus.observe(this) { status ->
            when (status) {
                is PostWriteViewModel.Status.Loading -> {
                    Toast.makeText(this, "게시물을 작성 중입니다...", Toast.LENGTH_SHORT).show()
                }
                is PostWriteViewModel.Status.Success -> {
                    Toast.makeText(this, "게시물 작성 성공!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is PostWriteViewModel.Status.Error -> {
                    Toast.makeText(this, "게시물 작성 실패: ${status.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.etAddress.setOnClickListener {
            val addressBottomSheet = AddressSearchBottomSheet.newInstance()
            addressBottomSheet.show(supportFragmentManager, "AddressSearchBottomSheet")
        }

        binding.etDestination.setOnClickListener {
            val addressBottomSheet = AddressSearchBottomSheet.newInstance()
            addressBottomSheet.show(supportFragmentManager, "AddressSearchBottomSheet")
        }
    }

    fun setAddress(address: String) {
        Log.d("PostWriteActivity", "Address Received: $address")
        binding.etAddress.setText(address)
    }

    private fun loadAuthorName(authorId: String, callback: (String) -> Unit) {
        firestore.collection("users").document(authorId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val authorName = document.getString("name") ?: "Unknown Name"
                    callback(authorName)
                } else {
                    Log.e("FirestoreError", "Document not found for authorId: $authorId")
                    callback("Unknown Name")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching author name: ${exception.message}")
                callback("Unknown Name")
            }
    }
}