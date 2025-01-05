package com.together.togetherproject.presentation.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.together.togetherproject.data.model.Post
import com.together.togetherproject.databinding.ItemRealtimePostBinding

class RealTimePostAdapter(
    private var posts: List<Post>,
    private val onPostClick: (Post) -> Unit // 클릭 이벤트 콜백 추가
) : RecyclerView.Adapter<RealTimePostAdapter.RealTimePostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealTimePostViewHolder {
        val binding = ItemRealtimePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RealTimePostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RealTimePostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
        holder.itemView.setOnClickListener { onPostClick(post) } // 클릭 이벤트 설정
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    class RealTimePostViewHolder(private val binding: ItemRealtimePostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post // XML의 `post` 변수에 데이터 전달
            binding.executePendingBindings() // 즉시 바인딩 적용
        }
    }
}