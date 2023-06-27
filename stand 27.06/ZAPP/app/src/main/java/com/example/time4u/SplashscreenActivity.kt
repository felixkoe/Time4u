package com.example.time4u

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashscreenActivity : AppCompatActivity() {
    private val delayMillis = 2000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val handler = Handler(Looper.getMainLooper())


        handler.postDelayed({
            startLoginActivity()
            finish()
        }, delayMillis)

    }

    private fun startLoginActivity() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}