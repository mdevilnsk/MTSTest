package ru.plamit.mtstest.backend.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor: Interceptor{

    companion object {
        private const val USER_AGENT_KEY = "User-Agent"
        private const val TOKEN_KEY = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        var builder: Request.Builder = original.newBuilder()
                .header(TOKEN_KEY, "token 2ece82dfac4745c36226757ba4b4f588424d6007")
                .header(USER_AGENT_KEY, "Test-Android-Application")

        builder = builder.method(original.method(), original.body())
        return chain.proceed(builder.build())
    }
}