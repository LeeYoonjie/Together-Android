package com.together.togetherproject.util

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.together.togetherproject.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .placeholder(R.mipmap.profile) // 적합한 플레이스홀더 이미지 설정
        .error(R.drawable.ic_launcher_foreground) // 오류 시 대체 이미지 설정
        .transition(DrawableTransitionOptions.withCrossFade()) // 부드러운 전환 애니메이션
        .into(view)
}

@BindingAdapter("formattedTimestamp")
fun bindFormattedTimestamp(view: TextView, timestamp: Long?) {
    timestamp?.let {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = Date(it)
        view.text = sdf.format(date)
    } ?: run {
        view.text = ""
    }
}

@BindingAdapter("participantsFormatted")
fun setParticipantsFormatted(view: TextView, participants: List<String>?) {
    view.text = participants?.joinToString(separator = " and ") ?: "No participants"
}

@BindingAdapter("formattedParticipants")
fun bindFormattedParticipants(view: TextView, participants: List<String>?) {
    participants?.let {
        view.text = it.joinToString(separator = " and ")
    } ?: run {
        view.text = "No participants"
    }
}