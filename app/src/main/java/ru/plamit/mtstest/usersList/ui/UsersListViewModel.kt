package ru.plamit.mtstest.usersList.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ru.plamit.mtstest.backend.api.ResponseException
import ru.plamit.mtstest.usersList.IUsersLisViewModel
import ru.plamit.mtstest.usersList.IUsersListInteractor
import ru.plamit.mtstest.usersList.IUsersListRouter

class UsersListViewModel(
        private val interactor: IUsersListInteractor) : ViewModel(), IUsersLisViewModel {
    override val viewState: MutableLiveData<List<UserViewItem>> = MutableLiveData()

    var router: IUsersListRouter? = null

    var selectedUser: String = "octocat"

    @SuppressLint("CheckResult")
    override fun selectUser(login: String) {
        selectedUser = login
        interactor.getSubscribers(login)
                .flattenAsObservable { it }
                .flatMap { subscriber ->
                    interactor.getUserInformation(subscriber.login)
                            .map { user ->
                                UserViewItem(
                                        user.avatarUrl,
                                        user.login,
                                        user.name,
                                        user.company,
                                        user.email
                                )
                            }
                            .toObservable()
                }
                .toList()
                .subscribe({
                    viewState.postValue(it)
                }, {
                    if (it is ResponseException)
                    router?.routeToError(it.message ?: "unknown error")
                })
    }
}
