package com.fajarbaihaqi.githubapi.data.retrofit

import com.fajarbaihaqi.githubapi.data.response.GithubResponse
import com.fajarbaihaqi.githubapi.data.response.ItemsItem
import com.fajarbaihaqi.githubapi.data.response.UserDetailResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("search/users")
    fun getUser(
        @Query("q") query: String
    ):Call<GithubResponse>

    @GET("users/{username}")
    fun getUserDetail(
         @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
    @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
    @Path("username") username: String
    ): Call<List<ItemsItem>>

}

