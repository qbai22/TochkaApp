package com.example.tochkaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.data.users.allusers.GithubAllUsersDataSource
import com.example.tochkaapp.data.users.allusers.GithubAllUsersDataSourceFactory
import com.example.tochkaapp.data.users.searchedusers.GithubSearchedUsersDataSourceFactory
import com.example.tochkaapp.utils.LoadingState
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Vladimir Kraev
 */
class GitHubUsersRepository(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper
) : UsersRepository {

    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    private val pagedListConfig: PagedList.Config = setupConfig()

    override fun getUsers(): LiveData<PagedList<GithubUser>> {
        val factory = GithubAllUsersDataSourceFactory(compositeDisposable, githubApi, mapper)
        return LivePagedListBuilder(factory, pagedListConfig).setInitialLoadKey(0).build()
    }

    override fun searchUsers(query: String): LiveData<PagedList<GithubUser>> {
        val factory =
            GithubSearchedUsersDataSourceFactory(compositeDisposable, githubApi, mapper, query)
        return LivePagedListBuilder(factory, pagedListConfig).build()
    }

    override fun observeLoading(): LiveData<LoadingState> = loadingState


    private fun setupConfig() =
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setPageSize(PAGE_SIZE)
            .build()


    override fun close() {
        compositeDisposable.clear()
    }

    companion object {
        private const val TAG = "GIT_HUB_REPO"
        private const val PAGE_SIZE = 30
    }
}