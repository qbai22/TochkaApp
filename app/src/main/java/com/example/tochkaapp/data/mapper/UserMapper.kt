package com.example.tochkaapp.data.mapper

import com.example.tochkaapp.data.model.GithubUser
import com.example.tochkaapp.data.http.dto.DtoUser
import com.example.tochkaapp.data.http.dto.DtoSearchResponse

/**
 * Created by Vladimir Kraev
 */
interface UserMapper {

    //mapping users w/o a query request
    fun mapUsers(dtoUsers: List<DtoUser>): List<GithubUser>

    //mapping users with a query request
    fun mapUsers(dtoSearchResponse: DtoSearchResponse) : List<GithubUser>

}