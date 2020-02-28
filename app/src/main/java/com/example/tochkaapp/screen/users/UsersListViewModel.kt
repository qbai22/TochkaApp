package com.example.tochkaapp.screen.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.tochkaapp.UsersApp
import com.example.tochkaapp.data.model.User
import com.example.tochkaapp.data.repository.UsersRepository
import com.example.tochkaapp.utils.LoadingState
import javax.inject.Inject

/**
 * Created by Vladimir Kraev
 */
class UsersListViewModel : ViewModel() {

    @Inject
    lateinit var repository: UsersRepository

    init {
        UsersApp.instance.getDataComponent().inject(this@UsersListViewModel)
    }

    val users: LiveData<PagedList<User>> = repository.loadUsers(null)
    val loadingState: LiveData<LoadingState> = repository.loadingState

    fun onQueryChanged(queryString: String) {
        repository.loadUsers(queryString)
    }

    fun retry() {
        repository.retryLoad()
    }

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }

    companion object {
        private const val TAG = "USERS_LIST_VM"
    }
}