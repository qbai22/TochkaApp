package com.example.tochkaapp.data.repository

import androidx.paging.PagedList
import com.example.tochkaapp.data.model.User

/**
 * Created by Vladimir Kraev
 */
interface UsersRepository : Closable,
    Loadable {

    fun getUsers(query: String?): PagedList<User>

}