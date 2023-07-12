package com.example.time4you

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Profile::class], version = 1)
abstract class ProfileDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {
        private var instance: ProfileDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): ProfileDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, ProfileDatabase::class.java,
                    "profile_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        fun populateDatabase(db: ProfileDatabase) {
            val profileDao = db.profileDao()
            subscribeOnBackground {
                profileDao.insert(Profile("Felix", "Koelling", "Male",0,0,0,0))
                profileDao.insert(Profile("Yakup", "Cilesiz", "Male",0,0,0,0))

            }
        }
    }



}