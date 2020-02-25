package com.example.tochkaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.data.users.allusers.GithubAllUsersDataSourceFactory
import com.example.tochkaapp.data.users.searchedusers.GithubSearchedUsersDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Vladimir Kraev
 */
class GitHubUsersRepository(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper
) : UsersRepository {


    override fun observeUsers(): LiveData<PagedList<GithubUser>> {
        val config = getAllUsersPagedListConfig()
        val factory = GithubAllUsersDataSourceFactory(compositeDisposable, githubApi, mapper)
        val pagedList =
            LivePagedListBuilder<Long, GithubUser>(factory, config).setInitialLoadKey(0).build()
        return pagedList
    }

    override fun searchUsers(query: String): LiveData<PagedList<GithubUser>> {
        val config = getAllUsersPagedListConfig()
        val factory =
            GithubSearchedUsersDataSourceFactory(compositeDisposable, githubApi, mapper, query)
        val pagedList = LivePagedListBuilder<Int, GithubUser>(factory, config).build()
        return pagedList
    }

    private fun getAllUsersPagedListConfig() =
        PagedList.Config.Builder().setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setPageSize(PAGE_SIZE)
            .build()

    override fun close() {
        compositeDisposable.clear()
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}