package com.example.tochkaapp.data.repository.factory

import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.source.GithubAllUsersDataSource
import com.example.tochkaapp.data.repository.source.GithubSearchedUsersDataSource
import com.example.tochkaapp.data.repository.source.RetriableDataSource

/**
 * Created by Vladimir Kraev
 */

class GithubUsersDataSourceFactory(
    private val api: GithubApi,
    private val mapper: UserMapper
) : UsersDataSourcesFactory {

    override fun createAllUsersDataSource(): RetriableDataSource<User> =
        GithubAllUsersDataSource(api, mapper)

    override fun createSearchedUsersDataSource(query: String): RetriableDataSource<User> =
        GithubSearchedUsersDataSource(api, mapper, query)

}