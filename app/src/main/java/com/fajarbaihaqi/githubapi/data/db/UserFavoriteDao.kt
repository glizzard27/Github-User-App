package com.fajarbaihaqi.githubapi.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (userFavorite: UserFavorite)

    @Delete
    fun delete (userFavorite: UserFavorite)

    @Query("SELECT * FROM userfavorite ORDER BY username ASC")
    fun getAllFavoriteUsers(): LiveData<List<UserFavorite>>

    @Query("SELECT EXISTS(SELECT * FROM userfavorite WHERE username= :username AND isFavorite = 1)")
    fun isFavorite(username: String): Boolean
}