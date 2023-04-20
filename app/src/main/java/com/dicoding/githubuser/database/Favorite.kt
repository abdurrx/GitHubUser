package com.dicoding.githubuser.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Favorite (
    @PrimaryKey
    val username: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
) : Parcelable
