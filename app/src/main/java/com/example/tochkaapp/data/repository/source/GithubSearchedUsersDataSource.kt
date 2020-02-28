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
) : RetriableDataSource<User>() {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    override val loadingState = MutableLiveData<LoadingState>()
    override val initialLoadingState = MutableLiveData<LoadingState>()

    private val compositeDisposable = CompositeDisposable()

    // keep Completable reference for the retry event
    private var retryCompletable: Completable? = null

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

    override fun close() {
        compositeDisposable.clear()
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<User>) {
        loadingState.postValue(LoadingState.LOADING)

        Log.e(TAG, "load initial called")
        compositeDisposable.add(
            githubApi.searchUsers(query, lastRequestedPage, SIZE_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { mapper.mapUsers(it) }
                .doOnError { Log.e(TAG, "load initial error occured ${it.message}") }
                .subscribe({ users ->
                    // clear retry since last request succeeded
                    setRetry(null)
                    this.loadingState.postValue(LoadingState.LOADED)
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
        compositeDisposable.add(
            githubApi.searchUsers(query, lastRequestedPage, SIZE_PER_PAGE)
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

    companion object {
        private const val TAG = "SEARCHED_DATA_SOURCE"
        private const val SIZE_PER_PAGE = 30
    }

}