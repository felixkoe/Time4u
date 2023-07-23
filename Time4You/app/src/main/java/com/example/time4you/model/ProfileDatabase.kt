package com.example.time4you.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Profile::class], version = 2,  exportSchema = false)
abstract class ProfileDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {
        private var instance: ProfileDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): ProfileDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, ProfileDatabase::class.java,
                    "profile_database")
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
             override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE profile_table ADD COLUMN profilePic INTEGER NOT NULL DEFAULT 0")
            }
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
                profileDao.insert(Profile("Felix", "Koelling", "Male",0,0,0,0, 0))
                profileDao.insert(Profile("Yakup", "Cilesiz", "Male",0,0,0,1, 0))

            }
        }

    }



}