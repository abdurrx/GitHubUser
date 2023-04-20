package com.dicoding.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.githubuser.SettingPreferences
import com.dicoding.githubuser.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel(application: Application, private val pref: SettingPreferences): AndroidViewModel(application) {
    private val _listUser = MutableLiveData<List<ResponseItem>>()
    val listUser: LiveData<List<ResponseItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    init { getUsers("") }

    fun getUsers(username: String) {
        _isLoading.value = true
        val query = if(username.isEmpty()) USERNAME else username
        val client = ApiConfig.getApiService().getList(query)
        client.enqueue(object : Callback<Responses> {
            override fun onResponse(
                call: Call<Responses>,
                response: Response<Responses>
            ) {
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.user
                    _isLoading.value = false
                } else {
                    _toastMessage.value = "onFailure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Responses>, t: Throwable) {
                _toastMessage.value = "Error: ${t.message.toString()}"
                _isLoading.value = false
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    companion object{
        private const val USERNAME = "abdurrx"
    }
}

class ListViewModelFactory(private val application: Application, private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(application, pref) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}