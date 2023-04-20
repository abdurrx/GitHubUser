package com.dicoding.githubuser.repository

import android.content.Context
import android.util.Log
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.database.FavoriteRoom

class FavoriteRepository(context: Context) {
    private val dao = FavoriteRoom.getInstance(context).favoriteDao()

    fun getFav() = dao.getAll()

    fun getFavBy(username: String) = dao.getBy(username)

    fun insertFav(favorite: Favorite) {
        val exist = dao.getBy(favorite.username)
        Log.d("Favorite", "{$exist}")
        if(exist != null) {
            Log.d("Favorite", "User Already Exist!")
        }
        else {
            dao.insert(favorite)
        }
    }

    fun deleteFav(username: String) = dao.deleteBy(username)
}