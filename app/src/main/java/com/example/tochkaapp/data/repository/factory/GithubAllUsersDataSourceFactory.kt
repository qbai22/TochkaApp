package com.example.tochkaapp.data.repository.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.source.Loadable
import com.example.tochkaapp.data.repository.source.allusers.GithubAllUsersDataSource
import io.reactivex.disposables.CompositeDisposable

class GithubAllUsersDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper
) : LoadableDataSourceFactory<Long, User>() {

    override val loadable: MutableLiveData<Loadable> = MutableLiveData()

    override fun create(): DataSource<Long, User> {
        val usersDataSource =
            GithubAllUsersDataSource(
                compositeDisposable,
                githubApi,
                mapper
            )
        loadable.postValue(usersDataSource)
        return usersDataSource
    }

}
