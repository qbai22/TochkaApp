package com.example.tochkaapp.data.repository.source.allusers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
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

class GithubAllUsersDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val githubApi: GithubApi,
    private val mapper: UserMapper
) : ItemKeyedDataSource<Long, GithubUser>() {

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    private val _initialLoadingState = MutableLiveData<LoadingState>()
    val initialLoadingState: LiveData<LoadingState>
        get() = _initialLoadingState

    /**
     * Keep Completable reference for the retry event
     */
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

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<GithubUser>
    ) {
        _loadingState.postValue(LoadingState.LOADING)
        _initialLoadingState.postValue(LoadingState.LOADING)

        compositeDisposable.add(
            githubApi.getUsers(1, params.requestedLoadSize)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    Log.e(TAG, "users loaded size ${users.size}")
                    setRetry(null)
                    _loadingState.postValue(LoadingState.LOADED)
                    _initialLoadingState.postValue(LoadingState.LOADED)
                    callback.onResult(users)
                }, { throwable ->
                    Log.e(TAG, "error occured ${throwable.message}")
                    setRetry(Action { loadInitial(params, callback) })
                    val error = LoadingState.error(throwable.message)
                    _loadingState.postValue(error)
                    _initialLoadingState.postValue(error)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<GithubUser>) {
        _loadingState.postValue(LoadingState.LOADING)
        compositeDisposable.add(
            githubApi.getUsers(params.key, params.requestedLoadSize)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    setRetry(null)
                    _loadingState.postValue(LoadingState.LOADED)
                    callback.onResult(users)
                }, { throwable ->
                    setRetry(Action { loadAfter(params, callback) })
                    _loadingState.postValue(LoadingState.error(throwable.message))
                })
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<GithubUser>) {}

    override fun getKey(item: GithubUser): Long {
        return item.id
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    companion object {
        private const val TAG = "ALL_USERS_SOURCE"
    }

}