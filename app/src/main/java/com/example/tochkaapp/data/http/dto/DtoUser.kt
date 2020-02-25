package com.example.tochkaapp.data.http.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Vladimir Kraev
 */
data class DtoUser(

    var login: String,

    var id: Long,

    @SerializedName("node_id")
    var nodeId: String,


    @SerializedName("avatar_url")
    var avatarUrl: String,

    @SerializedName("gravatar_id")
    var gravatarId: String,

    var url: String,

    @SerializedName("html_url")
    var htmlUrl: String,

    @SerializedName("followers_url")
    var followersUrl: String,

    @SerializedName("following_url")
    var followingUrl: String,

    @SerializedName("gists_url")
    var gistsUrl: String,

    @SerializedName("starred_url")
    var starredUrl: String,

    @SerializedName("subscriptions_url")
    var subscriptionsUrl: String,

    @SerializedName("organizations_url")
    var organizationsUrl: String,

    @SerializedName("repos_url")
    var reposUrl: String,

    @SerializedName("events_url")
    var eventsUrl: String,

    @SerializedName("received_events_url")
    var receivedEventsUrl: String,

    var type: String,

    @SerializedName("site_admin")
    var siteAdmin: Boolean,

    var score: Int? = 0

    /*
     login: "yydcdut",
     id: 7722279,
     node_id: "MDQ6VXNlcjc3MjIyNzk=",
     avatar_url: "https://avatars2.githubusercontent.com/u/7722279?v=4",
     gravatar_id: "",
     url: "https://api.github.com/users/yydcdut",
     html_url: "https://github.com/yydcdut",
     followers_url: "https://api.github.com/users/yydcdut/followers",
     following_url: "https://api.github.com/users/yydcdut/following{/other_user}",
     gists_url: "https://api.github.com/users/yydcdut/gists{/gist_id}",
     starred_url: "https://api.github.com/users/yydcdut/starred{/owner}{/repo}",
     subscriptions_url: "https://api.github.com/users/yydcdut/subscriptions",
     organizations_url: "https://api.github.com/users/yydcdut/orgs",
     repos_url: "https://api.github.com/users/yydcdut/repos",
     events_url: "https://api.github.com/users/yydcdut/events{/privacy}",
     received_events_url: "https://api.github.com/users/yydcdut/received_events",
     type: "User",
     site_admin: false,
     score: 1
   */

)