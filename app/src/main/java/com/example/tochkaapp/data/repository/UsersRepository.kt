package com.example.tochkaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.tochkaapp.data.model.User

/**
 * Created by Vladimir Kraev
 */
interface UsersRepository :
    ClosableRepository,
    LoadingProgressRepository {

    fun getUsers(): PagedList<User>

    fun getAllUsers(): LiveData<PagedList<User>>

    fun searchUsers(query: String): LiveData<PagedList<User>>

}