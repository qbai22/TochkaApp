package com.example.tochkaapp.data.repository.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.utils.LoadingState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vladimir Kraev
 */

class GithubSearchedUsersDataSource(
    private val githubApi: GithubApi,
    private val mapper: UserMapper,
    private val query: String
) : RepeatableDataSource<User>() {

    override val loadingState = MutableLiveData<LoadingState>()

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1
    private val compositeDisposable = CompositeDisposable()

    // keep Completable reference for the repeat call
    private var repeatCompletable: Completable? = null

    override fun repeatLastCall() {
        repeatCompletable?.let {
            compositeDisposable.add(
                it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { })
            )
        }
    }

    override fun close() {
        compositeDisposable.clear()
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<User>) {
        loadingState.postValue(LoadingState.LOADING)

        Log.e(TAG, "load initial called")
        compositeDisposable.add(
            githubApi.searchUsers(query, lastRequestedPage, params.pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { mapper.mapUsers(it) }
                .doOnError { Log.e(TAG, "load initial error occured ${it.message}") }
                .subscribe({ users ->
                    //we don't need to repeat after successful call
                    setRepeat(null)
                    Log.e(TAG, "users loaded size = ${users.size}")
                    this.loadingState.postValue(LoadingState.LOADED)
                    callback.onResult(users, 0)
                }, { throwable ->
                    // keep a Completable for future repeat
                    setRepeat(Action { loadInitial(params, callback) })
                    loadingState.postValue(LoadingState.error(throwable.message))
                })
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<User>) {
        loadingState.postValue(LoadingState.LOADING)
        Log.e(TAG, "load range called")
        compositeDisposable.add(
            githubApi.searchUsers(query, lastRequestedPage, params.loadSize)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    setRepeat(null)
                    loadingState.postValue(LoadingState.LOADED)
                    lastRequestedPage++
                    callback.onResult(users)
                }, { throwable ->
                    setRepeat(Action { loadRange(params, callback) })
                    loadingState.postValue(LoadingState.error(throwable.message))
                })
        )
    }


    private fun setRepeat(action: Action?) {
        repeatCompletable = if (action == null) null
        else Completable.fromAction(action)
    }

    companion object {
        private const val TAG = "SEARCHED_DATA_SOURCE"
    }

}