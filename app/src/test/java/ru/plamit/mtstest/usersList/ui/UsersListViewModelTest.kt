package ru.plamit.mtstest.usersList.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import ru.plamit.mtstest.backend.api.ResponseException
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.entity.GithubUserSubscriber
import ru.plamit.mtstest.usersList.IUsersListInteractor
import ru.plamit.mtstest.usersList.IUsersListRouter

@Suppress("NonAsciiCharacters", "UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class UsersListViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UsersListViewModel
    private val interactor = mock(IUsersListInteractor::class.java)
    private val viewStateObserver = mock(Observer::class.java
//            , withSettings().verboseLogging()
    ) as Observer<List<UserViewItem>>
    private val router = mock(IUsersListRouter::class.java)

    @Before
    fun setUp() {
        viewModel = UsersListViewModel(interactor)
        viewModel.router = router
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(interactor)
        verifyNoMoreInteractions(router)
        verifyNoMoreInteractions(viewStateObserver)
    }

    @Test
    fun `should get subscribers and theirs information`() {
        //precondition
        `when`(interactor.getSubscribers(ArgumentMatchers.anyString()))
                .thenReturn(Single.just(generateSubscribers(2)))
        `when`(interactor.getUserInformation("login_1"))
                .thenReturn(Single.just(generateUserInformation(1)))
        `when`(interactor.getUserInformation("login_2"))
                .thenReturn(Single.just(generateUserInformation(2)))

        //action
        viewModel.viewState.observeForever(viewStateObserver)
        viewModel.selectUser("octocat")

        //result
        verify(interactor).getSubscribers("octocat")
        verify(interactor).getUserInformation("login_1")
        verify(interactor).getUserInformation("login_2")
        verify(viewStateObserver).onChanged(generateUserItems(2))
    }

    @Test
    fun `should return error if can not get users subscriber`() {
        //precondition
        val error = ResponseException(404, "Not Found")
        `when`(interactor.getSubscribers(ArgumentMatchers.anyString()))
                .thenReturn(Single.error(error))

        //action
        viewModel.viewState.observeForever(viewStateObserver)
        viewModel.selectUser("octocat")

        //result
        verify(interactor).getSubscribers("octocat")
        verify(router).routeToError(error.message!!)
    }

    @Test
    fun `should return error when can't get user information`() {
        //precondition
        val error = ResponseException(404, "Not Found")
        `when`(interactor.getSubscribers(ArgumentMatchers.anyString()))
                .thenReturn(Single.just(generateSubscribers(2)))
        `when`(interactor.getUserInformation(anyString()))
                .thenReturn(Single.error(error))

        //action
        viewModel.viewState.observeForever(viewStateObserver)
        viewModel.selectUser("octocat")

        //result
        verify(interactor).getSubscribers("octocat")
        verify(interactor).getUserInformation("login_1")
        verify(router).routeToError(error.message!!)
    }

    //region generate informations
    private fun generateUserItems(num: Int): List<UserViewItem> {
        val users = ArrayList<UserViewItem>()
        for (i in 1..num) {
            users.add(generateUserItem(i))
        }
        return users
    }

    private fun generateUserItem(id: Int) = UserViewItem(
            "avatarUrl_$id",
            "login_$id",
            "name_$id",
            "company_$id",
            "email_$id"
    )

    private fun generateSubscriber(id: Int) = GithubUserSubscriber(
            "gistsUrl_$id",
            "reposUrl_$id",
            "followingUrl_$id",
            "starredUrl_$id",
            "login_$id",
            "followersUrl_$id",
            "type_$id",
            "url_$id",
            "subscriptionsUrl_$id",
            "receivedEventsUrl_$id",
            "avatarUrl_$id",
            "eventsUrl_$id",
            "htmlUrl_$id",
            id % 2 == 0,
            id,
            "gravatarId_$id",
            "nodeId_$id",
            "organizationsUrl_$id"
    )

    private fun generateSubscribers(num: Int): List<GithubUserSubscriber> {
        val subscribers = ArrayList<GithubUserSubscriber>()
        for (i in 1..num) {
            subscribers.add(generateSubscriber(i))
        }
        return subscribers
    }

    private fun generateUserInformation(id: Int) = GithubUserInformation(
            "gistsUrl_$id",
            "reposUrl_$id",
            "followingUrl_$id",
            "bio_$id",
            "createdAt_$id",
            "login_$id",
            "type_$id",
            "blog_$id",
            "subscriptionsUrl_$id",
            "updatedAt_$id",
            id % 2 == 0,
            "company_$id",
            id,
            id,
            "gravatarId_$id",
            "email_$id",
            "organizationsUrl_$id",
            "hireable_$id",
            "starredUrl_$id",
            "followersUrl_$id",
            id,
            "url_$id",
            "receivedEventsUrl__$id",
            id,
            "avatarUrl_$id",
            "eventsUrl_$id",
            "htmlUrl_$id",
            id,
            "name_$id"
    )
    //endregion
}