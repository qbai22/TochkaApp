package com.example.tochkaapp.data.repository.factory

import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.User
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Vladimir Kraev
 */

class GithubUsersDataSourceFactory(
   private val api: GithubApi,
   private val mapper: UserMapper
) : UsersDataSourcesFactory {
    
    override fun createAllUsersDataSourceFactory(): LoadableDataSourceFactory<Long, User> =
        GithubAllUsersDataSourceFactory(
            CompositeDisposable(),
            api,
            mapper
        )

    override fun createSearchedUsersDataSourceFactory(query: String): LoadableDataSourceFactory<Int, User> =
        GithubSearchedUsersDataSourceFactory(
            CompositeDisposable(),
            api,
            mapper,
            query
        )

}