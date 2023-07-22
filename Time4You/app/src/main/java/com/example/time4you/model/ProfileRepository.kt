package com.example.time4you.model

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
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
//        Single.just(noteDao.insert(note))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe()
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

    suspend fun deletePoints(userId: Int, points: Int) = withContext(Dispatchers.IO) {
        val user = profileDao.getUserById(userId).first()
        user?.let {
            it.pointsNow -= points
            if (it.pointsNow < 0) it.pointsNow = 0
            profileDao.update(it)
        }
    }

}