package com.example.tochkaapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.data.repository.source.allusers.GithubAllUsersDataSource
import com.example.tochkaapp.data.repository.source.allusers.GithubAllUsersDataSourceFactory
import com.example.tochkaapp.data.repository.source.searchedusers.GithubSearchedUsersDataSourceFactory
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

    private var loadingState: LiveData<LoadingState> = MutableLiveData()
    private val pagedListConfig: PagedList.Config = setupConfig()

    override fun getUsers(): LiveData<PagedList<GithubUser>> {
        Log.e(TAG, "CALLED GET USERS")
        clear()
        val factory = GithubAllUsersDataSourceFactory(compositeDisposable, githubApi, mapper)

        loadingState = Transformations.switchMap<GithubAllUsersDataSource, LoadingState>(
            factory.dataSourceValue, { it.loadingState })

        return LivePagedListBuilder(factory, pagedListConfig).setInitialLoadKey(0).build()
    }

    override fun searchUsers(query: String): LiveData<PagedList<GithubUser>> {
        Log.e(TAG, "CALLED SEARCHED USERS QUERY = $query")
        clear()
        val factory =
            GithubSearchedUsersDataSourceFactory(compositeDisposable, githubApi, mapper, query)
        return LivePagedListBuilder(factory, pagedListConfig).build()
    }

    override fun observeLoading(): LiveData<LoadingState> = loadingState

 /*   fun getPagedList(): PagedList<GithubUser> {
        val allUsersDataSource = GithubAllUsersDataSource(compositeDisposable, githubApi, mapper)
        val pagedList: PagedList<GithubUser> = PagedList.Builder(allUsersDataSource, pagedListConfig).build()
        return pagedList
    }*/

    private fun setupConfig() =
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setPageSize(PAGE_SIZE)
            .build()


    override fun close() {
        compositeDisposable.clear()
    }

    private fun clear(){
        compositeDisposable.clear()
    }

    companion object {
        private const val TAG = "GIT_HUB_REPO"
        private const val PAGE_SIZE = 30
    }
}