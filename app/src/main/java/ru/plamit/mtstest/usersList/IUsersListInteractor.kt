package ru.plamit.mtstest.usersList

import io.reactivex.Single
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.entity.GithubUserSubscriber

/**
 * Users list interactor (use cases)
 */
interface IUsersListInteractor{
    /**
     * get github subscribers list
     * @param githubLogin - github user login
     */
    fun getSubscribers(githubLogin: String): Single<List<GithubUserSubscriber>>
    /**
     * get github user information
     * @param githubLogin - github user login
     */
    fun getUserInformation(githubLogin: String): Single<GithubUserInformation>
}