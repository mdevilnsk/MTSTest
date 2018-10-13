package ru.plamit.mtstest.usersList

import io.reactivex.Single
import ru.plamit.mtstest.backend.api.IGithubApi
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.entity.GithubUserSubscriber
import ru.plamit.mtstest.utils.IDefaultScheduler

class UsersListInteractor(
        private val api: IGithubApi,
        private val scheduler: IDefaultScheduler
) : IUsersListInteractor {

    override fun getSubscribers(githubLogin: String): Single<List<GithubUserSubscriber>> =
            api.getUserFollowers(githubLogin).compose(scheduler.applySingle())

    override fun getUserInformation(githubLogin: String): Single<GithubUserInformation> =
            api.getUserInformation(githubLogin).compose(scheduler.applySingle())
}