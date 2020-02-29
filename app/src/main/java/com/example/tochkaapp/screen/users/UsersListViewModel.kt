package com.example.tochkaapp.screen.users

import androidx.lifecycle.LiveData
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

    private val queryDisp: Disposable
    //subject to react on query change with a delay
    private val querySubject = PublishSubject.create<String>()

    init {
        UsersApp.instance.getDataComponent().inject(this@UsersListViewModel)

        queryDisp = querySubject
            .debounce(400, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { repository.loadUsers(it) }
    }

    val users: LiveData<PagedList<User>> = repository.loadUsers(null)
    val loadingState: LiveData<LoadingState> = repository.loadingState


    fun onQuerySubmit(queryString: String) {
        repository.loadUsers(queryString)
    }

    fun onQueryChanged(query: String) {
        querySubject.onNext(query.trim())
    }

    fun retry() {
        repository.retryLoad()
    }


    override fun onCleared() {
        super.onCleared()
        repository.close()
        queryDisp.dispose()
    }

    companion object {
        private const val TAG = "USERS_LIST_VM"
    }
}