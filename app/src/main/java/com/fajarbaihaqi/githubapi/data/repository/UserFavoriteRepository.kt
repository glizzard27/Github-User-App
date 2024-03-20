package com.fajarbaihaqi.githubapi.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.fajarbaihaqi.githubapi.data.db.UserFavorite
import com.fajarbaihaqi.githubapi.data.db.UserFavoriteDao
import com.fajarbaihaqi.githubapi.data.db.UserFavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserFavoriteRepository(application: Application) {
    private val mUserFavoritesDao: UserFavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserFavoriteRoomDatabase.getDatabase(application)
        mUserFavoritesDao = db.userFavoriteDao()
    }

    fun getAllFavorite(): LiveData<List<UserFavorite>> = mUserFavoritesDao.getAllFavoriteUsers()

    fun getIsFavorite(username: String): Boolean {
        return mUserFavoritesDao.isFavorite(username)
    }

    fun insert(favorite: UserFavorite) {
        executorService.execute { mUserFavoritesDao.insert(favorite) }
    }

    fun delete(favorite: UserFavorite) {
        executorService.execute { mUserFavoritesDao.delete(favorite) }
    }
}