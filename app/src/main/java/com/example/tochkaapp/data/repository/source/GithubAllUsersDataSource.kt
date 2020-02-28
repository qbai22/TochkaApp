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
) : RetriableDataSource<User>() {

    override val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    override val initialLoadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    private var lastRequestedPage = 0

    // Keep Completable reference for the retry event
    private var retryCompletable: Completable? = null

    override fun close() {
        compositeDisposable.clear()
    }

    override fun retry() {
        retryCompletable?.let {
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
        val since = calculateLastUserId()
        Log.e(TAG, "load initial called")
        compositeDisposable.add(
            githubApi.getUsers(since, SIZE_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    // clear retry since last request succeeded
                    setRetry(null)
                    loadingState.postValue(LoadingState.LOADED)
                    Log.e(TAG, "USERS DOWNLOADED SIZE = ${users.size}")
                    callback.onResult(users, 0)
                }, { throwable ->
                    // keep a Completable for future retry
                    setRetry(Action { loadInitial(params, callback) })
                    loadingState.postValue(LoadingState.error(throwable.message))
                })
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<User>) {
        loadingState.postValue(LoadingState.LOADING)
        val since = calculateLastUserId()
        compositeDisposable.add(
            githubApi.getUsers(since, SIZE_PER_PAGE)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    setRetry(null)
                    loadingState.postValue(LoadingState.LOADED)
                    lastRequestedPage++
                    callback.onResult(users)
                }, { throwable ->
                    setRetry(Action { loadRange(params, callback) })
                    loadingState.postValue(LoadingState.error(throwable.message))
                })
        )
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    private fun calculateLastUserId(): Long = (lastRequestedPage * SIZE_PER_PAGE).toLong() + 1

    companion object {
        private const val TAG = "ALL_USERS_SOURCE"
        private const val SIZE_PER_PAGE = 30
    }

}