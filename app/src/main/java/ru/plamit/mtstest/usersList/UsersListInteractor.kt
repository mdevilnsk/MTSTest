package ru.plamit.mtstest.usersList

import io.reactivex.Single
import ru.plamit.mtstest.api.IGithubApi
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.utils.IDefaultScheduler

class UsersListInteractor(
        private val api: IGithubApi,
        private val scheduler: IDefaultScheduler
) : IUsersListInteractor {

    override fun getSubscribers(githubLogin: String): Single<List<GithubUserInformation>> =
        api.getUserFollowers(githubLogin).compose(scheduler.applySingle())
}