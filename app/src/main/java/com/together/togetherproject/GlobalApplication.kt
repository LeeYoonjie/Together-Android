package com.together.togetherproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "{37eb95ced531c5e2316d8d6a84c810fc}")
        KakaoMapSdk.init(this, "37eb95ced531c5e2316d8d6a84c810fc");
    }
}