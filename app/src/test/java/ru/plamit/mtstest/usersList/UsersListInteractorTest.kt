package ru.plamit.mtstest.usersList

import io.reactivex.Single
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import ru.plamit.mtstest.backend.api.IGithubApi
import ru.plamit.mtstest.backend.api.ResponseException
import ru.plamit.mtstest.backend.storage.Storage
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.entity.GithubUserSubscriber
import ru.plamit.mtstest.utils.DefaultSchedulerTest

@Suppress("NonAsciiCharacters", "UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class UsersListInteractorTest {

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    private lateinit var interactor: UsersListInteractor

    private val api = mock(IGithubApi::class.java)
    private val testScheduler = DefaultSchedulerTest()
    private val storage = mock(Storage::class.java)


    @After
    fun tearDown() {
        verify(storage).userInformation
        verifyNoMoreInteractions(api)
        verifyNoMoreInteractions(storage)
    }

    @Test
    fun `should request list of users`() {
        interactor = UsersListInteractor(api, testScheduler, storage)
        //precondition
        val login = "octocat"
        `when`(api.getUserFollowers(anyString())).thenReturn(Single.just(generateGithubUsers(5)))
        //action
        val testObserver = interactor.getSubscribers(login).test()
        Thread.sleep(20)
        //result
        verify(api).getUserFollowers(login)
        testObserver.assertValue(generateGithubUsers(5))
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun `should return error`() {
        interactor = UsersListInteractor(api, testScheduler, storage)
        //precondition
        val login = "octocat1"
        val error = Throwable("error")
        `when`(api.getUserFollowers(anyString())).thenReturn(Single.error(error))
        //action
        val testObserver = interactor.getSubscribers(login).test()
        //result
        verify(api).getUserFollowers(login)
        testObserver.assertError(error)
    }

    @Test
    fun `should request user information`() {
        interactor = UsersListInteractor(api, testScheduler, storage)
        //precondition
        `when`(api.getUserInformation(anyString())).thenReturn(Single.just(generateUserInformation(1)))

        //action
        val observer = interactor.getUserInformation("login_1").test()
        Thread.sleep(20)
        //result
        verify(storage).userInformation
        verify(storage).userInformation = any()
        verify(api).getUserInformation("login_1")
        observer.assertValue(generateUserInformation(1))
    }

    @Test
    fun `should return saved value`() {
        //precondition
        `when`(storage.userInformation).thenReturn(arrayListOf(generateUserInformation(1)))
        interactor = UsersListInteractor(api, testScheduler, storage)
        //action
        val observer = interactor.getUserInformation("login_1").test()
        Thread.sleep(20)
        //result
        verify(storage).userInformation
        observer.assertValue(generateUserInformation(1))
    }

    @Test
    fun `should return error on user information request`(){
        interactor = UsersListInteractor(api, testScheduler, storage)
        //precondition
        val error = ResponseException(404,"Not Found")
        `when`(api.getUserInformation(anyString())).thenReturn(Single.error(error))

        //action
        val observer = interactor.getUserInformation("login_1").test()
        Thread.sleep(20)
        //result
        verify(storage, atLeastOnce()).userInformation
        verify(api).getUserInformation("login_1")
        observer.assertError(error)
    }

    //region generate
    private fun generateGithubUsers(num: Int): List<GithubUserSubscriber> {
        val users = ArrayList<GithubUserSubscriber>()
        for (i in 0..num) {
            users.add(generateGithubUser(num))
        }
        return users
    }

    private fun generateGithubUser(id: Int) = GithubUserSubscriber(
            "gists_$id",
            "repos_$id",
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
            "gravetarUrl_$id",
            "nodeId_$id",
            "organizationsUrl_$id"
    )

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