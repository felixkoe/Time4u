package com.example.time4you

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDao {

    @Insert
    fun insert(profile: Profile)

    @Update
    fun update(profile: Profile)

    @Delete
    fun delete(profile: Profile)

    @Query("delete from profile_table")
    fun deleteAllProfiles()

    @Query("select * from profile_table order by userId desc")
    fun getAllProfiles(): LiveData<List<Profile>>
}