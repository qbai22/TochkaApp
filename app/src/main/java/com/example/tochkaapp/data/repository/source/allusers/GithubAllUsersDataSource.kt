package com.example.tochkaapp.data.repository.source.allusers

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
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        _loadingState.postValue(LoadingState.LOADING)
        _initialLoadingState.postValue(LoadingState.LOADING)

        //get the initial users from the api
        compositeDisposable.add(
            githubApi.getUsers(1, params.requestedLoadSize)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    // clear retry since last request succeeded
                    setRetry(null)
                    _loadingState.postValue(LoadingState.LOADED)
                    _initialLoadingState.postValue(LoadingState.LOADED)
                    callback.onResult(users)
                }, { throwable ->
                    // keep a Completable for future retry
                    setRetry(Action { loadInitial(params, callback) })
                    val error = LoadingState.error(throwable.message)
                    // publish the error
                    _loadingState.postValue(error)
                    _initialLoadingState.postValue(error)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<GithubUser>) {
        // set network value to loading.
        _loadingState.postValue(LoadingState.LOADING)

        //get the users from the api after id
        compositeDisposable.add(
            githubApi.getUsers(params.key, params.requestedLoadSize)
                .map { mapper.mapUsers(it) }
                .subscribe({ users ->
                    // clear retry since last request succeeded
                    setRetry(null)
                    _loadingState.postValue(LoadingState.LOADED)
                    callback.onResult(users)
                }, { throwable ->
                    // keep a Completable for future retry
                    setRetry(Action { loadAfter(params, callback) })
                    // publish the error
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

}