package com.example.tochkaapp.utils

import com.example.tochkaapp.data.model.GithubUser

/**
 * Created by Vladimir Kraev
 */

/**
 * Class to observe navigation event to User Details Screen
 */
class NavigateToDetailsEvent(user: GithubUser) : Event<GithubUser>(user)
