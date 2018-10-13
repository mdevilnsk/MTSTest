package ru.plamit.mtstest.usersList

import android.arch.lifecycle.MutableLiveData
import ru.plamit.mtstest.usersList.ui.UserViewItem

interface IUsersLisViewModel{
    /**
     * live data with selected user state
     */
    val viewState: MutableLiveData<List<UserViewItem>>

    /**
     * select user to get his followers
     * @param login - user login
     */
    fun selectUser(login: String)
}