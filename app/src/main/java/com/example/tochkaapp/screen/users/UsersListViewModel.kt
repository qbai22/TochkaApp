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
import com.example.tochkaapp.utils.NavigateToDetailsEvent
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

    private val _navigateToContactDetailsEvent = MutableLiveData<NavigateToDetailsEvent>()
    val navigateToContactDetailsEvent: LiveData<NavigateToDetailsEvent> =
        _navigateToContactDetailsEvent

    private val queryLiveData = MutableLiveData<String>()

    val allUsers: LiveData<PagedList<GithubUser>> = repository.getUsers()
    val loadingState: LiveData<LoadingState> = repository.observeLoading()

    val searchedUsers: LiveData<PagedList<GithubUser>> =
        Transformations.switchMap(queryLiveData) { repository.searchUsers(it) }

    fun retry() {}

    fun searchUsers(queryString: String) {
        queryLiveData.value = queryString
    }

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }
}