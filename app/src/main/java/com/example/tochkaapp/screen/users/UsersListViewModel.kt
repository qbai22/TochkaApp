package com.example.tochkaapp.screen.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.tochkaapp.UsersApp
import com.example.tochkaapp.data.model.GithubUser
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

    val allUsers: LiveData<PagedList<GithubUser>> = repository.getAllUsers()
    val searchedUsers: LiveData<PagedList<GithubUser>> =
        Transformations.switchMap(queryLiveData) { repository.searchUsers(it) }

    val loadingState: LiveData<LoadingState> = repository.observeLoading()

    fun onQueryChanged(queryString: String) {
        queryLiveData.value = queryString
    }

    fun retry() {}

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }

    companion object {
        private const val TAG = "USERS_LIST_VM"
    }
}