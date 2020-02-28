package com.example.tochkaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.factory.UsersDataSourcesFactory
import com.example.tochkaapp.data.repository.source.Loadable
import com.example.tochkaapp.data.repository.source.MainThreadExecutor
import com.example.tochkaapp.utils.LoadingState
import java.util.concurrent.Executors

/**
 * Created by Vladimir Kraev
 */

class GitHubUsersRepository(
    private val usersDataSourceFactory: UsersDataSourcesFactory
) : UsersRepository {

    private val loadingState: MediatorLiveData<LoadingState> = MediatorLiveData()
    private val pagedListConfig: PagedList.Config = setupConfig()

    override fun getAllUsers(): LiveData<PagedList<User>> {
        clear()
        val factory = usersDataSourceFactory.createAllUsersDataSourceFactory()

        val allUsersLoadingState = Transformations.switchMap<Loadable, LoadingState>(
            factory.loadable, { it.loadingState })

        loadingState.addSource(allUsersLoadingState) { loadingState.value = it }
        return LivePagedListBuilder(factory, pagedListConfig).setInitialLoadKey(0).build()
    }

    override fun searchUsers(query: String): LiveData<PagedList<User>> {
        clear()
        val factory = usersDataSourceFactory.createSearchedUsersDataSourceFactory(query)

        val searchedUsersLoadingState = Transformations.switchMap<Loadable, LoadingState>(
            factory.loadable, { it.loadingState })

        loadingState.addSource(searchedUsersLoadingState) { loadingState.value = it }
        return LivePagedListBuilder(factory, pagedListConfig).build()
    }

    override fun getUsers(): PagedList<User> {
        return buildPagedList()
    }

    private fun buildPagedList(): PagedList<User> {
        val allUsersDataSource = usersDataSourceFactory.createAllUsersDataSourceFactory().create()
        val pagedList = PagedList.Builder(allUsersDataSource, pagedListConfig)
            .setNotifyExecutor(MainThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()

        return pagedList
    }

    override fun observeLoading() = loadingState

    private fun setupConfig() =
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()


    override fun close() {
        clear()
    }

    private fun clear() {

    }

    companion object {
        private const val TAG = "GIT_HUB_REPO"
        private const val PAGE_SIZE = 30
    }
}