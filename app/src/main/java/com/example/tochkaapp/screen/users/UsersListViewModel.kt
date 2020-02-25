package com.example.tochkaapp.screen.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.tochkaapp.UsersApp
import com.example.tochkaapp.data.di.DataComponent
import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.data.repository.UsersRepository
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

    val users: LiveData<PagedList<GithubUser>> = repository.observeUsers()
    fun retry() {}


    override fun onCleared() {
        super.onCleared()
        repository.close()
    }
}