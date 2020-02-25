package com.example.tochkaapp.data.mapper

import com.example.tochkaapp.data.http.dto.DtoUser
import com.example.tochkaapp.data.http.dto.DtoSearchResponse
import com.example.tochkaapp.data.model.GithubUser

/**
 * Created by Vladimir Kraev
 */
class GithubUserMapper : UserMapper {

    override fun mapUsers(dtoUsers: List<DtoUser>): List<GithubUser> {
        val mappedUsers = ArrayList<GithubUser>()

        dtoUsers.forEach { mappedUsers.add(mapUser(it)) }

        return mappedUsers
    }

    override fun mapUsers(usersSearchResponse: DtoSearchResponse): List<GithubUser> {
        val mappedUsers = ArrayList<GithubUser>()

        usersSearchResponse.users.forEach { mappedUsers.add(mapUser(it)) }

        return mappedUsers
    }

    private fun mapUser(dtoUser: DtoUser): GithubUser {
        val id = dtoUser.id
        val name = dtoUser.login
        val avatarUrl = dtoUser.avatarUrl
        val repositoriesUrl = dtoUser.reposUrl
        val starredUrl = dtoUser.starredUrl

        return GithubUser(
            id,
            name,
            avatarUrl,
            repositoriesUrl,
            starredUrl
        )
    }

}