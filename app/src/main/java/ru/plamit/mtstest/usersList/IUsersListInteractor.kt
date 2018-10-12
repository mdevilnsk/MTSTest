package ru.plamit.mtstest.usersList

import io.reactivex.Single
import ru.plamit.mtstest.entity.GithubUserInformation

/**
 * Users list interactor (use cases)
 */
interface IUsersListInteractor{
    /**
     * get github subscribers list
     * @param githubLogin - github user login
     */
    fun getSubscribers(githubLogin: String): Single<List<GithubUserInformation>>
}