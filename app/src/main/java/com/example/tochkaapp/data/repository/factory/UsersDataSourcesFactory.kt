package com.example.tochkaapp.data.repository.factory

import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.source.RepeatableDataSource

/**
 * Created by Vladimir Kraev
 */
interface UsersDataSourcesFactory {

    fun createAllUsersDataSource(): RepeatableDataSource<User>

    fun createSearchedUsersDataSource(query: String): RepeatableDataSource<User>

}