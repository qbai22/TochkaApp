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

class GithubAllUsersDataSource(
    private val githubApi: GithubApi,
    private val mapper: UserMapper
) : RepeatableDataSource<User>() {

    override val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    override val initialLoadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()
    private var lastUserId = 0L

    // Keep Completable reference for the repeat event
    private var repeatCompletable: Completable? = null

    override fun close() {
        compositeDisposable.clear()
    }

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

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<User>) {
        loadingState.postValue(LoadingState.LOADING)
        Log.e(TAG, "load initial called")
        compositeDisposable.add(
            githubApi.getUsers(lastUserId, params.pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { mapper.mapUsers(it) }
                .doOnSuccess { lastUserId = it.last().id }
                .subscribe({ users ->
                    //we don't need to repeat after successful call
                    setRepeat(null)
                    loadingState.postValue(LoadingState.LOADED)
                    Log.e(TAG, "USERS DOWNLOADED SIZE = ${users.size}")
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
        compositeDisposable.add(
            githubApi.getUsers(lastUserId, params.loadSize)
                .map { mapper.mapUsers(it) }
                .doOnSuccess { lastUserId = it.last().id }
                .subscribe({ users ->
                    setRepeat(null)
                    loadingState.postValue(LoadingState.LOADED)
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
        private const val TAG = "ALL_USERS_SOURCE"
    }

}