package com.fajarbaihaqi.githubapi.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fajarbaihaqi.githubapi.data.db.UserFavorite
import com.fajarbaihaqi.githubapi.data.repository.UserFavoriteRepository
import com.fajarbaihaqi.githubapi.data.response.ItemsItem
import com.fajarbaihaqi.githubapi.data.response.UserDetailResponse
import com.fajarbaihaqi.githubapi.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {
    private val userFavoriteRepository: UserFavoriteRepository = UserFavoriteRepository(application)

    private val _userDetail = MutableLiveData<UserFavorite>()
    val userDetail: LiveData<UserFavorite> = _userDetail

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loadingScreen

    private val _userFollower = MutableLiveData<List<ItemsItem>>()
    val userFollower: LiveData<List<ItemsItem>> = _userFollower

    private val _userFollowing = MutableLiveData<List<ItemsItem>>()
    val userFollowing: LiveData<List<ItemsItem>> = _userFollowing

    private var isloaded = false
    private var isfollowerloaded = false
    private var isfollowingloaded = false

    companion object{
        private const val TAG = "DetailUserViewModel"
    }

    fun getDetailUser(username: String) {
        if (!isloaded){
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getUserDetail(username)
            client.enqueue(object: Callback<UserDetailResponse>{
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody != null){
                            viewModelScope.launch {
                                val isFavorite = userFavoriteRepository.getIsFavorite(responseBody.login)
                                val userCurrent = UserFavorite(
                                    username = responseBody.login,
                                    name = responseBody.name,
                                    avatarUrl = responseBody.avatarUrl,
                                    followersCount = responseBody.followers.toString(),
                                    followingCount = responseBody.following.toString(),
                                    isFavorite = isFavorite
                                )
                                _userDetail.postValue(userCurrent)
                            }
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isloaded = true
        }
    }

    fun getUserFollowing(username: String){
        if (!isfollowingloaded){
            _loadingScreen.value = true
            val client =  ApiConfig.getApiService().getUserFollowing(username)
            client.enqueue(object : Callback<List<ItemsItem>>{
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        _userFollowing.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isfollowingloaded = true
        }
    }

    fun getUserFollower(username: String){
        if(!isfollowerloaded){
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getUserFollowers(username)
            client.enqueue(object : Callback<List<ItemsItem>>{
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        _userFollower.postValue(response.body())

                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isfollowerloaded = true
        }
    }

    fun insertFavorite(userFavorite: UserFavorite){
        userFavoriteRepository.insert(userFavorite)
    }

    fun deleteFavorite(userFavorite: UserFavorite){
        userFavoriteRepository.delete(userFavorite)
    }
}