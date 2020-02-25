package com.example.tochkaapp.data.model

import java.io.Serializable

/**
 * Created by Vladimir Kraev
 */

data class GithubUser(

    var id: Long,

    var name: String,

    var avatarUrl: String,

    var repositoriesUrl: String,

    var starredUrl: String

) : Serializable