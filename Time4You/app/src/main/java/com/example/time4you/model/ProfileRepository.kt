package com.example.time4you.model

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.time4you.controller.subscribeOnBackground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first




class ProfileRepository(application: Application) {

    private var profileDao: ProfileDao
    private var allProfiles: LiveData<List<Profile>>
    private val database = ProfileDatabase.getInstance(application)

    init {
        profileDao = database.profileDao()
        allProfiles = profileDao.getAllProfiles()
    }

    fun insert(profile: Profile) {
        subscribeOnBackground {
            profileDao.insert(profile)
        }
    }

    fun update(profile: Profile) {
        subscribeOnBackground {
            profileDao.update(profile)
        }
    }

    fun delete(profile: Profile) {
        subscribeOnBackground {
            profileDao.delete(profile)
        }
    }

    fun deleteAllProfiles() {
        subscribeOnBackground {
            profileDao.deleteAllProfiles()
        }
    }

    fun getAllProfiles(): LiveData<List<Profile>> {
        return allProfiles
    }

    suspend fun addPoints(userId: Int, points: Int) = withContext(Dispatchers.IO) {
        val user = profileDao.getUserById(userId).first()
        user?.let {
            it.pointsNow += points
            it.pointsAll += points
            profileDao.update(it)
        }
    }

    suspend fun changeLevel(userId: Int, level: Int) = withContext(Dispatchers.IO) {
        val user = profileDao.getUserById(userId).first()
        user?.let {
            it.level = level
            profileDao.update(it)
        }
    }

    suspend fun changePic(userId: Int, pic: Int) = withContext(Dispatchers.IO) {
        val user = profileDao.getUserById(userId).first()
        user?.let {
            it.profilePic = pic
            profileDao.update(it)
        }
    }

    suspend fun deletePoints(userId: Int, points: Int) = withContext(Dispatchers.IO) {
        val user = profileDao.getUserById(userId).first()
        user?.let {
            it.pointsNow -= points
            if (it.pointsNow < 0) it.pointsNow = 0
            profileDao.update(it)
        }
    }

    fun getProfileById(id: Int): Flow<Profile?> {
        return profileDao.getUserById(id)
    }
}