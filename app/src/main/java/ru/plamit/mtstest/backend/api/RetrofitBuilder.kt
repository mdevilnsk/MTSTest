package ru.plamit.mtstest.backend.api

import android.content.Context
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.plamit.mtstest.BuildConfig
import ru.plamit.mtstest.backend.api.IGithubApi.Companion.BASE_URL
import java.util.concurrent.TimeUnit

/**
 * Class for create api implementation of interface
 */
class RetrofitBuilder {

    companion object {
        private const val HTTP_LOG_TAG = "OkHttp"
        private const val DEFAULT_TIMEOUT = 20L
    }

    fun createApi(test: Boolean = false): IGithubApi {
        val httpClient = buildOkHttpClient(DEFAULT_TIMEOUT, test)

        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)

        return retrofitBuilder.build().create(IGithubApi::class.java)
    }

    fun createPicasso(ctx: Context): Picasso =
            Picasso.Builder(ctx)
                    .downloader(OkHttp3Downloader(buildOkHttpClient(DEFAULT_TIMEOUT, false)))
                    .build()

    private fun buildOkHttpClient(timeout: Long, test: Boolean): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(timeout, TimeUnit.SECONDS)
        readTimeout(timeout, TimeUnit.SECONDS)
        addInterceptor(BaseResponseInterceptor())
        addInterceptor(HeaderInterceptor())
        if (!test) addInterceptor(LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .tag(HTTP_LOG_TAG)
                .build())
    }.build()
}