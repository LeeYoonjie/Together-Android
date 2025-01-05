package com.together.togetherproject.presentation.post.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.together.togetherproject.data.model.Post
import com.together.togetherproject.databinding.ItemHotPostBinding
import com.together.togetherproject.presentation.post.view.PostDetailActivity

class HotPostAdapter(
    private var posts: List<Post>
) : RecyclerView.Adapter<HotPostAdapter.HotPostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotPostViewHolder {
        val binding = ItemHotPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotPostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    inner class HotPostViewHolder(private val binding: ItemHotPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
            binding.executePendingBindings()

            // 항목 클릭 시 PostDetailActivity로 이동
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, PostDetailActivity::class.java).apply {
                    putExtra("POST_ID", post.id) // Post ID 전달
                }
                context.startActivity(intent)
            }
        }
    }
}