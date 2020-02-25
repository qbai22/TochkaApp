package com.example.tochkaapp.data.http.api

import com.example.tochkaapp.data.http.dto.DtoSearchResponse
import com.example.tochkaapp.data.http.dto.DtoUser
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Vladimir Kraev
 */
interface GithubApi {

    @GET("/users?")
    fun getUsers(
        @Query("since") userId: Long,
        @Query("per_page") perPage: Int
    ): Single<List<DtoUser>>

    @GET("search/users?")
    fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<DtoSearchResponse>

}