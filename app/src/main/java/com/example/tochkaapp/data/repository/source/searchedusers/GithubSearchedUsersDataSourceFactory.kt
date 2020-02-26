package com.example.tochkaapp.data.repository.source.searchedusers

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.GithubUser
import io.reactivex.disposables.CompositeDisposable

class GithubSearchedUsersDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper,
    private val query: String
) : DataSource.Factory<Int, GithubUser>() {

    val dataSourceValue = MutableLiveData<GithubSearchedUsersDataSource>()

    override fun create(): DataSource<Int, GithubUser> {
        val usersDataSource = GithubSearchedUsersDataSource(compositeDisposable, githubApi, mapper, query)
        dataSourceValue.postValue(usersDataSource)
        return usersDataSource
    }

}
