package com.dicoding.githubuser.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getList(
        @Query("q") username: String
    ): Call<Responses>

    @GET("users/{username}")
    fun getDetail(
        @Path("username") username: String
    ): Call<ResponseItem>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username: String
    ): Call<List<ResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ResponseItem>>
}