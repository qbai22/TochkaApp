package com.example.tochkaapp.screen.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val queryLiveData = MutableLiveData<String>()

    private val _usersData: MutableLiveData<PagedList<User>> = MutableLiveData()
    val users: LiveData<PagedList<User>> = _usersData

    val loadingState: LiveData<LoadingState> = repository.loadingState

    fun onQueryChanged(queryString: String) {
        _usersData.value = repository.getUsers(queryString)
    }

    fun retry() {}

    fun init(){
        _usersData.value = repository.getUsers(query = null)
    }

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }

    companion object {
        private const val TAG = "USERS_LIST_VM"
    }
}