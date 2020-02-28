package com.example.tochkaapp.data.repository.factory

import com.example.tochkaapp.data.model.User

/**
 * Created by Vladimir Kraev
 */
interface UsersDataSourcesFactory {

    fun createAllUsersDataSourceFactory(): LoadableDataSourceFactory<Long, User>

    fun createSearchedUsersDataSourceFactory(query: String): LoadableDataSourceFactory<Int, User>

}