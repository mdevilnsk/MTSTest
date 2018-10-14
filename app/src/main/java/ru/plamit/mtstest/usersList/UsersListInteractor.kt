package ru.plamit.mtstest.usersList

import io.reactivex.Single
import ru.plamit.mtstest.backend.api.IGithubApi
import ru.plamit.mtstest.backend.storage.Storage
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.entity.GithubUserSubscriber
import ru.plamit.mtstest.utils.IDefaultScheduler

class UsersListInteractor(
        private val api: IGithubApi,
        private val scheduler: IDefaultScheduler,
        private val storage: Storage
) : IUsersListInteractor {

    var users: MutableList<GithubUserInformation> = ArrayList()

    init {
        val nonNullElements = storage.userInformation
        nonNullElements.filter {
            @Suppress("SENSELESS_COMPARISON")
            it != null
        }.let {
            users.addAll(it)
        }
    }

    override fun getSubscribers(githubLogin: String): Single<List<GithubUserSubscriber>> =
            api.getUserFollowers(githubLogin).compose(scheduler.applySingle())

    override fun getUserInformation(githubLogin: String): Single<GithubUserInformation> {
        if (users.asSequence().map { it.login }.contains(githubLogin))
            return Single.just(users.find { it.login == githubLogin }).compose(scheduler.applySingle())

        return api.getUserInformation(githubLogin)
                .doOnSuccess {
                    users.add(it)
                    storage.userInformation = users
                }
                .compose(scheduler.applySingle())
    }
}