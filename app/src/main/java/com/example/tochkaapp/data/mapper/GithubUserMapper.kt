package com.example.tochkaapp.data.mapper

import com.example.tochkaapp.data.http.dto.DtoUser
import com.example.tochkaapp.data.http.dto.DtoSearchResponse
import com.example.tochkaapp.data.model.User

/**
 * Created by Vladimir Kraev
 */
class GithubUserMapper : UserMapper {

    override fun mapUsers(dtoUsers: List<DtoUser>): List<User> {
        val mappedUsers = ArrayList<User>()

        dtoUsers.forEach { mappedUsers.add(mapUser(it)) }

        return mappedUsers
    }

    override fun mapUsers(dtoSearchResponse: DtoSearchResponse): List<User> {
        val mappedUsers = ArrayList<User>()

        dtoSearchResponse.users.forEach { mappedUsers.add(mapUser(it)) }

        return mappedUsers
    }

    private fun mapUser(dtoUser: DtoUser): User {
        val id = dtoUser.id
        val name = dtoUser.login
        val avatarUrl = dtoUser.avatarUrl
        val profileUrl = dtoUser.htmlUrl
        return User(
            id,
            name,
            avatarUrl,
            profileUrl

        )
    }

}