package com.example.time4you

import android.app.Application
import androidx.lifecycle.LiveData


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



}