package com.example.time4you.model

import android.app.Application
import com.example.time4you.model.ProfileDatabase.Companion.getInstance

class MyApplication : Application() {

    lateinit var database: ProfileDatabase

    override fun onCreate() {
        super.onCreate()

        database = getInstance(this)
    }
}