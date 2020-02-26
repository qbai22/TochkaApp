package com.example.tochkaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.tochkaapp.data.model.GithubUser

/**
 * Created by Vladimir Kraev
 */
interface UsersRepository :
    ClosableRepository,
    LoadingProgressRepository
{

    fun getUsers(): LiveData<PagedList<GithubUser>>

    fun searchUsers(query: String): LiveData<PagedList<GithubUser>>

}