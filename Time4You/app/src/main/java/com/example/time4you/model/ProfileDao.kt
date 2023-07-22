package com.example.time4you.model

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


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

    @Query("SELECT * FROM profile_table WHERE userId = :id")
    fun getUserById(id: Int): Flow<Profile?>
}