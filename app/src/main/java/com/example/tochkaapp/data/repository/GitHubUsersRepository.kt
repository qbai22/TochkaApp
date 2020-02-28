package com.example.tochkaapp.data.repository

import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.factory.UsersDataSourcesFactory
import com.example.tochkaapp.data.repository.source.RetriableDataSource
import com.example.tochkaapp.utils.LoadingState
import java.util.concurrent.Executors

/**
 * Created by Vladimir Kraev
 */

class GitHubUsersRepository(
    private val usersDataSourceFactory: UsersDataSourcesFactory
) : UsersRepository {

    override val loadingState: MediatorLiveData<LoadingState> = MediatorLiveData()
    override val initialLoadingState: MediatorLiveData<LoadingState> = MediatorLiveData()

    override fun getUsers(query: String?) =
        if (query.isNullOrEmpty()) getAllUsers() else searchUsers(query)


    private fun getAllUsers(): PagedList<User> {
        clear()
        val dataSource = usersDataSourceFactory.createAllUsersDataSource()
        loadingState.addSource(dataSource.loadingState) { loadingState.value = it }

        return buildUsersPagedList(dataSource)
    }


    private fun searchUsers(query: String): PagedList<User> {
        clear()
        val dataSource = usersDataSourceFactory.createSearchedUsersDataSource(query)

        loadingState.addSource(dataSource.loadingState) { loadingState.value = it }
        return buildUsersPagedList(dataSource)
    }

    private fun buildUsersPagedList(dataSource: RetriableDataSource<User>) =
        PagedList.Builder(dataSource, setupConfig())
            .setNotifyExecutor(MainThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()

    private fun setupConfig() =
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .build()


    override fun close() {
        clear()
    }

    private fun clear() {

    }

    companion object {
        private const val TAG = "GIT_HUB_REPO"
    }
}