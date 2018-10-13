package ru.plamit.mtstest.backend.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.entity.GithubUserSubscriber

interface IGithubApi{
    companion object {
        const val BASE_URL = "https://api.github.com/"
    }

    @GET("users/{userLogin}/followers")
    fun getUserFollowers(@Path("userLogin") userLogin: String): Single<List<GithubUserSubscriber>>

    @GET("users/{userLogin}")
    fun getUserInformation(@Path("userLogin") userLogin: String): Single<GithubUserInformation>
}