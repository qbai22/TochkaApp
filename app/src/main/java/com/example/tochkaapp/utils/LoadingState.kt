package com.example.tochkaapp.utils

/**
 * Created by Vladimir Kraev
 */

enum class State {
    LOADING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class LoadingState private constructor(
    val state: State,
    val message: String? = null
) {

    companion object {

        val LOADING = LoadingState(State.LOADING)
        val LOADED = LoadingState(State.SUCCESS)
        fun error(msg: String?) = LoadingState(State.FAILED, msg)

    }
}
