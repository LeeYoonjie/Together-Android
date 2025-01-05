package com.together.togetherproject.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.together.togetherproject.R
import com.together.togetherproject.MainActivity
import com.together.togetherproject.presentation.signin.SignInActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent= Intent( this, SignInActivity::class.java)
            startActivity(intent)

            finish()

        }, 3000)
    }
}