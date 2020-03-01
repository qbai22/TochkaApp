package com.example.tochkaapp.screen.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.tochkaapp.UsersApp
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.UsersRepository
import com.example.tochkaapp.utils.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Vladimir Kraev
 */
class UsersListViewModel : ViewModel() {

    @Inject
    lateinit var repository: UsersRepository

    private val queryDisposable: Disposable

    //subject to react on query change with a delay
    private val querySubject = PublishSubject.create<String>()

    //live data to transform query in a repository call
    private val queryLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        UsersApp.instance.getDataComponent().inject(this@UsersListViewModel)

        queryDisposable = querySubject
            .debounce(400, TimeUnit.MILLISECONDS)
            .doOnNext { Log.e(TAG,"subject onNext value = $it") }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { queryLiveData.value = it }
    }

    val users: LiveData<PagedList<User>> =
        Transformations.switchMap(queryLiveData) { query -> repository.loadUsers(query) }

    val loadingState: LiveData<LoadingState> = repository.loadingState

    fun onQueryChanged(query: String) = querySubject.onNext(query.trim())

    fun retry() = repository.retryLoad()

    override fun onCleared() {
        super.onCleared()
        repository.close()
        queryDisposable.dispose()
    }

    companion object {
        private const val TAG = "USERS_LIST_VM"
    }
}