package com.example.tochkaapp.data.repository.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.source.Loadable
import com.example.tochkaapp.data.repository.source.searchedusers.GithubSearchedUsersDataSource
import io.reactivex.disposables.CompositeDisposable

class GithubSearchedUsersDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper,
    private val query: String
) : LoadableDataSourceFactory<Int, User>() {

    override val loadable: MutableLiveData<Loadable> = MutableLiveData()

    override fun create(): DataSource<Int, User> {
        val usersDataSource =
            GithubSearchedUsersDataSource(
                compositeDisposable,
                githubApi,
                mapper,
                query
            )
        loadable.postValue(usersDataSource)
        return usersDataSource
    }


}
