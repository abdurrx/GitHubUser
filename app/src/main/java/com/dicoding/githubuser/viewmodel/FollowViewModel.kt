package com.dicoding.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.api.ApiConfig
import com.dicoding.githubuser.api.ResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel: ViewModel() {
    private val _listFollow = MutableLiveData<List<ResponseItem>>()
    val listFollow: LiveData<List<ResponseItem>> = _listFollow

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun getFollower(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollower(username)
        client.enqueue(object : Callback<List<ResponseItem>> {
            override fun onResponse(
                call: Call<List<ResponseItem>>,
                response: Response<List<ResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _listFollow.value = response.body()
                    _isLoading.value = false
                } else {
                    _toastMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                _toastMessage.value = "Error: ${t.message.toString()}"
                _isLoading.value = false
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ResponseItem>> {
            override fun onResponse(
                call: Call<List<ResponseItem>>,
                response: Response<List<ResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _listFollow.value = response.body()
                    _isLoading.value = false
                } else {
                    _toastMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ResponseItem>>, t: Throwable) {
                _toastMessage.value = "Error: ${t.message.toString()}"
                _isLoading.value = false
            }
        })
    }
}