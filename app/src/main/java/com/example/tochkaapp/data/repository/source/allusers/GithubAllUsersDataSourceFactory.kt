package com.example.tochkaapp.data.repository.source.allusers

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.GithubUser
import io.reactivex.disposables.CompositeDisposable

class GithubAllUsersDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper
) : DataSource.Factory<Long, GithubUser>() {

    val dataSourceValue = MutableLiveData<GithubAllUsersDataSource>()

    override fun create(): DataSource<Long, GithubUser> {
        val usersDataSource = GithubAllUsersDataSource(compositeDisposable, githubApi, mapper)
        dataSourceValue.postValue(usersDataSource)
        return usersDataSource
    }

}
