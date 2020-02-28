package com.example.tochkaapp.screen.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
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

    private val _allUsersData: MutableLiveData<PagedList<User>> = MutableLiveData()
    val allUsers: LiveData<PagedList<User>> = _allUsersData

    val searchedUsers: LiveData<PagedList<User>> =
        Transformations.switchMap(queryLiveData) { repository.searchUsers(it) }

    val loadingState: LiveData<LoadingState> = repository.observeLoading()

    fun onQueryChanged(queryString: String) {
        queryLiveData.value = queryString
    }

    fun retry() {}

    fun init(){
        _allUsersData.value = repository.getUsers()
    }

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }

    companion object {
        private const val TAG = "USERS_LIST_VM"
    }
}