package com.example.time4u

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashscreenActivity : AppCompatActivity() {
    private val delayMillis = 4000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)


        Handler().postDelayed({
            startMainActivity()
            finish()
        }, delayMillis)

    }

    private fun startMainActivity() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}