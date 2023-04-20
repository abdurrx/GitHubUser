package com.dicoding.githubuser.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorite WHERE username = :username")
    fun getBy(username: String): Favorite?

    @Insert
    fun insert(users: Favorite)

    @Query("DELETE FROM favorite WHERE username = :username")
    fun deleteBy(username: String)
}