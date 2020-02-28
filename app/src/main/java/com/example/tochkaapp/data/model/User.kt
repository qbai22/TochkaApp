package com.example.tochkaapp.data.model

import java.io.Serializable

/**
 * Created by Vladimir Kraev
 */

data class User(

    var id: Long,

    var name: String,

    var avatarUrl: String,

    var profileUrl: String

) : Serializable