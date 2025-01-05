package com.together.togetherproject.data.model

data class MatchedItem(
    val matchedRoomId: String = "",
    val user1Name: String = "",
    val user2Name: String = "",
    val startAddress: String = "",
    val destination: String = "",
    val startTime: String = "",
    val accountNumber: String = "",
    val paymentAmount: String = "",
    val individualPayment: String = "", // 개인 결제 금액 추가
    val timestamp: Long = System.currentTimeMillis()
)

data class MatchHistory(
    val userId: String = "",
    val chatRoomId: String = "",
    val recipientId: String = "",
    val timestamp: Long = 0L
)