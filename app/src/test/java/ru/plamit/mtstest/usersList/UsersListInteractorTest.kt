package ru.plamit.mtstest.usersList

import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.junit.MockitoJUnitRunner
import ru.plamit.mtstest.api.IGithubApi
import ru.plamit.mtstest.entity.GithubUserInformation
import ru.plamit.mtstest.utils.DefaultSchedulerTest

@Suppress("NonAsciiCharacters", "UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class UsersListInteractorTest {

    private lateinit var interactor: UsersListInteractor

    private val api = Mockito.mock(IGithubApi::class.java)
    private val testScheduler = DefaultSchedulerTest()

    @Before
    fun setUp() {
        interactor = UsersListInteractor(api, testScheduler)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(api)
    }

    @Test
    fun `should request list of users`() {
        //precondition
        val login = "octocat"
        Mockito.`when`(api.getUserFollowers(anyString())).thenReturn(Single.just(generateGithubUsers(5)))
        //action
        val testObserver = interactor.getSubscribers(login).test()
        //result
        Mockito.verify(api).getUserFollowers(login)
        testObserver.assertValue(generateGithubUsers(5))
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun `should return error`() {
        //precondition
        val login = "octocat1"
        val error = Throwable("error")
        Mockito.`when`(api.getUserFollowers(anyString())).thenReturn(Single.error(error))
        //action
        val testObserver = interactor.getSubscribers(login).test()
        //result
        Mockito.verify(api).getUserFollowers(login)
        testObserver.assertError(error)
    }

    private fun generateGithubUsers(num: Int): List<GithubUserInformation> {
        val users = ArrayList<GithubUserInformation>()
        for (i in 0..num) {
            users.add(generateGithubUser(num))
        }
        return users
    }

    private fun generateGithubUser(id: Int) = GithubUserInformation(
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
}