package com.together.togetherproject.data.model

data class Comment(
    var id: String = "",              // Firestore 문서 ID
    var postId: String = "",          // 게시물 ID
    var content: String = "",         // 댓글 내용
    var timestamp: Long = 0L,         // 작성 시간
    var authorId: String = "",        // 작성자 ID
    var authorNickname: String = ""   // 작성자 닉네임
)