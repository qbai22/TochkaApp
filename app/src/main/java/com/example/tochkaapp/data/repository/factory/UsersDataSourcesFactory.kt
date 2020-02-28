package com.example.tochkaapp.data.repository.factory

import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.source.RetriableDataSource

/**
 * Created by Vladimir Kraev
 */
interface UsersDataSourcesFactory {

    fun createAllUsersDataSource(): RetriableDataSource<User>

    fun createSearchedUsersDataSource(query: String): RetriableDataSource<User>

}