package com.together.togetherproject.data.model

data class ChatMessage(
    val id: String = "", // 메시지 ID
    val text: String = "", // 메시지 내용
    val timestamp: Long = System.currentTimeMillis(), // 메시지 생성 시간
    val senderId: String = "", // 보낸 사람 ID
    val recipientId: String = "", // 받는 사람 ID
    val roomId: String = "" // 채팅방 ID (어떤 방에 속하는 메시지인지 구분)
)

data class ChatRoom(
    val roomId: String = "", // 채팅방 ID
    val lastMessage: String = "", // 마지막 메시지
    val timestamp: Long = System.currentTimeMillis(), // 마지막 메시지 시간
    val participants: List<String> = emptyList() // 참여자 ID 리스트
)