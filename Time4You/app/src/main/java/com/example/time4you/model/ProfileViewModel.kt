package com.example.time4you.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {


    fun addPoints(userId: Int, points: Int) {
        viewModelScope.launch {
            repository.addPoints(userId, points)
        }
    }
}
