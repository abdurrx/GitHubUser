package com.dicoding.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.githubuser.api.ApiConfig
import com.dicoding.githubuser.api.ResponseItem
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): ViewModel() {
    private val favRepo = FavoriteRepository(application)

    private val _detail = MutableLiveData<ResponseItem>()
    val detail: LiveData<ResponseItem> = _detail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(username)
        client.enqueue(object : Callback<ResponseItem> {
            override fun onResponse(
                call: Call<ResponseItem>,
                response: Response<ResponseItem>
            ) {
                if (response.isSuccessful) {
                    _detail.value = response.body()
                    _isLoading.value = false
                } else {
                    _toastMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResponseItem>, t: Throwable) {
                _toastMessage.value = "Error: ${t.message.toString()}"
                _isLoading.value = false
            }
        })
    }

    fun insertFavorite(fav: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            favRepo.insertFav(fav)
            _isFavorite.postValue(true)
        }
    }

    fun isFav(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = favRepo.getFavBy(username)
            _isFavorite.postValue(user != null)
        }
    }

    fun deleteFavorite(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favRepo.deleteFav(username)
            _isFavorite.postValue(false)
        }
    }
}

class DetailViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}