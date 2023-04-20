package com.dicoding.githubuser.api

import com.google.gson.annotations.SerializedName

data class Responses(

	@field:SerializedName("items")
	val user: List<ResponseItem>
)

data class ResponseItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int,
)
