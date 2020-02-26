package com.example.tochkaapp.data.users.searchedusers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.model.GithubUser
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
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper,
    private val query: String
) : PositionalDataSource<GithubUser>() {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    private val _initialLoadingState = MutableLiveData<LoadingState>()
    val initialLoadingState: LiveData<LoadingState>
        get() = _initialLoadingState


    // keep Completable reference for the retry event
    private var retryCompletable: Completable? = null

    fun retry() {
        retryCompletable?.let {
            compositeDisposable.add(
                it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { })
            )
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GithubUser>) {
        _loadingState.postValue(LoadingState.LOADING)

        compositeDisposable.add(
            githubApi.searchUsers(query, lastRequestedPage, params.requestedLoadSize)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    // clear retry since last request succeeded
                    setRetry(null)
                    _loadingState.postValue(LoadingState.LOADED)
                    callback.onResult(users, 0)
                }, { throwable ->
                    // keep a Completable for future retry
                    setRetry(Action { loadInitial(params, callback) })
                    // publish the error
                    _loadingState.postValue(LoadingState.error(throwable.message))
                })
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<GithubUser>) {
        // set network value to loading.
        _loadingState.postValue(LoadingState.LOADING)

        compositeDisposable.add(
            githubApi.searchUsers(query, lastRequestedPage, params.loadSize)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    // clear retry since last request succeeded
                    setRetry(null)
                    _loadingState.postValue(LoadingState.LOADED)
                    Log.e(TAG, "loading range page $lastRequestedPage")
                    lastRequestedPage++
                    callback.onResult(users)
                }, { throwable ->
                    // keep a Completable for future retry
                    setRetry(Action { loadRange(params, callback) })
                    // publish the error
                    _loadingState.postValue(LoadingState.error(throwable.message))
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
    }

}