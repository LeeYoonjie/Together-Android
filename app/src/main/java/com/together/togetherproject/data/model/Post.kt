package com.together.togetherproject.data.model

data class Post(
    val id: String = "",              // 게시물 고유 ID
    val title: String = "",           // 게시물 제목
    val content: String = "",         // 게시물 내용
    val timestamp: Long = 0L,         // 작성 시간
    val authorId: String = "",        // 작성자 ID
    val authorName: String = "",      // 작성자 이름
    val startAddress: String = "",    // 출발지 주소
    val destination: String = "",     // 도착지 주소
    val views: Int = 0                // 조회수
)