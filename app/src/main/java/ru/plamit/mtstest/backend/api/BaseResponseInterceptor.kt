package ru.plamit.mtstest.backend.api

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.net.ConnectException
import java.net.UnknownHostException

class BaseResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response
        try {
            response = chain.proceed(chain.request())
        } catch (e: UnknownHostException) {
            throw RuntimeException("check internet connection")
        } catch (e: ConnectException) {
            throw RuntimeException("can't connect to server")
        } catch (e: Throwable){
            throw RuntimeException(e.localizedMessage ?: e.message ?: "unknown server error")
        }

        when (response.code()) {
            in 200..299 -> return response
            else -> {
                val baseResponse = Gson().fromJson(response.body()!!.string(), BaseErrorResponse::class.java)
                val e = ResponseException(response.code(), baseResponse.error
                        ?: baseResponse.message ?: "unknown error")
                throw e
            }
        }
    }
}