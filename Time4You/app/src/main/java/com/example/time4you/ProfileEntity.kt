package com.example.time4you

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class Profile(val firstName: String,
                   val lastName: String,
                   val gender: String,
                   var pointsNow: Int,
                   var pointsAll: Int,
                   var level: Int,
                   val userId: Int,
                @PrimaryKey(autoGenerate = false) val id: Int? = null)
