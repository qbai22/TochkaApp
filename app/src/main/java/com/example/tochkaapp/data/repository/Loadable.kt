package com.example.tochkaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tochkaapp.utils.LoadingState

/**
 * Created by Vladimir Kraev
 */

/** Indicates entity which can publish it's loading state via [LoadingState] class */
interface Loadable {

    val loadingState: LiveData<LoadingState>

}