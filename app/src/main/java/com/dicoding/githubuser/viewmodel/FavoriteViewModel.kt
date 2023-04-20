package com.dicoding.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.githubuser.database.Favorite
import com.dicoding.githubuser.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application): ViewModel() {
    private val favRepo = FavoriteRepository(application)

    private val _favorite = MutableLiveData<List<Favorite>>()
    val favorite: LiveData<List<Favorite>> = _favorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFav() {
        _isLoading.value = false
        viewModelScope.launch(Dispatchers.IO){
            val users = favRepo.getFav()
            _favorite.postValue(users)
        }
    }
}

class FavoriteViewModelFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}