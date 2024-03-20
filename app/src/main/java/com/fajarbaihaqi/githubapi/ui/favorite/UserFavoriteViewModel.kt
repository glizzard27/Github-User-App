package com.fajarbaihaqi.githubapi.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fajarbaihaqi.githubapi.data.db.UserFavorite
import com.fajarbaihaqi.githubapi.data.repository.UserFavoriteRepository

class UserFavoriteViewModel(application: Application) : ViewModel() {
    private val userFavoriteRepository: UserFavoriteRepository = UserFavoriteRepository(application)
    fun getAllFavoriteUsers(): LiveData<List<UserFavorite>> = userFavoriteRepository.getAllFavorite()
    init {
        getAllFavoriteUsers()
    }
}