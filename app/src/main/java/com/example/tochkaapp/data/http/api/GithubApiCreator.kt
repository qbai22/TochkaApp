package com.example.tochkaapp.data.http.api
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Vladimir Kraev
 */

class GithubApiCreator {

    private var githubApi: GithubApi? = null

    fun getApi(): GithubApi {

        if (githubApi != null) return githubApi!!

        val gson = GsonBuilder().create()

        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        githubApi = retrofit.create(GithubApi::class.java)

        return githubApi!!
    }

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        private const val TAG = "API_CREATOR"
    }

}