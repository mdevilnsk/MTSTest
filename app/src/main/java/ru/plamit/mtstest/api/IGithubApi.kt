package ru.plamit.mtstest.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IGithubApi{
    companion object {
        const val BASE_URL = "https://api.github.com/"
    }

    @GET("users/{userLogin}/followers")
    fun getUserFollowers(@Path("userLogin") userLogin: String): Single<List<String>>
}