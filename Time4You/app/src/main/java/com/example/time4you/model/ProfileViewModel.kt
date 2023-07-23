package com.example.time4you.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    val profile: Flow<Profile?> = repository.getProfileById(0)
    fun addPoints(userId: Int, points: Int) {
        viewModelScope.launch {
            repository.addPoints(userId, points)
        }
    }

    fun deletePoints(userId: Int, points: Int) {
        viewModelScope.launch {
            repository.deletePoints(userId, points)
        }
    }

    fun changeLevel(userId: Int, level: Int){
        viewModelScope.launch {
            repository.changeLevel(userId, level)
        }
    }

    fun changePic(userId: Int, pic: Int){
        viewModelScope.launch {
            repository.changePic(userId, pic)
        }
    }
}
