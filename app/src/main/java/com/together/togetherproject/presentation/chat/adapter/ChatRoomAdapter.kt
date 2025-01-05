package com.together.togetherproject.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.together.togetherproject.R
import com.together.togetherproject.data.model.ChatRoom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRoomAdapter(
    private var chatRooms: List<ChatRoom>,
    private val onRoomClick: (ChatRoom) -> Unit // 클릭 이벤트 핸들러
) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    inner class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomNameTextView: TextView = view.findViewById(R.id.tvChatRoomUsers)
        val lastMessageTextView: TextView = view.findViewById(R.id.tvLastMessage)
        val timestampTextView: TextView = view.findViewById(R.id.tvTime)

        fun bind(chatRoom: ChatRoom) {
            roomNameTextView.text = chatRoom.participants.joinToString(" and ")
            lastMessageTextView.text = chatRoom.lastMessage
            timestampTextView.text = formatTimestamp(chatRoom.timestamp)

            itemView.setOnClickListener {
                onRoomClick(chatRoom) // 클릭 이벤트 전달
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(chatRooms[position])
    }

    override fun getItemCount(): Int = chatRooms.size

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun updateData(newChatRooms: List<ChatRoom>) {
        chatRooms = newChatRooms
        notifyDataSetChanged()
    }
}