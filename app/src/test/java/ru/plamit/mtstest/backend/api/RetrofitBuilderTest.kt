package ru.plamit.mtstest.backend.api

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RetrofitBuilderTest{
    @Test
    fun `should request subscribers and print them`() {
        val latch = CountDownLatch(1)
        RetrofitBuilder()
                .createApi(true)
                .getUserFollowers("octocat")
                .subscribe({
                    System.out.println(it)
                    latch.countDown()
                    assertEquals("myhduck",it[0].login)
                }, {
                    latch.countDown()
                    it.printStackTrace()
                })

        latch.await(10, TimeUnit.SECONDS)
    }

    @Test
    fun `should return error for user`() {
        val latch = CountDownLatch(1)
        RetrofitBuilder()
                .createApi(true)
                .getUserFollowers("octocat223123")
                .subscribe({
                    System.out.println(it)
                    latch.countDown()
                }, {
                    assertEquals("Not Found", it.message)
                    latch.countDown()
                })

        latch.await(10, TimeUnit.SECONDS)
    }

}