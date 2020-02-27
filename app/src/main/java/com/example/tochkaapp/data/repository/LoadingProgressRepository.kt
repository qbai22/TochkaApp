package com.example.tochkaapp.data.repository

import androidx.lifecycle.LiveData
import com.example.tochkaapp.utils.LoadingState

/**
 * Created by Vladimir Kraev
 */
/** Repository which is support delivering loading progress through [LoadingState] */

interface LoadingProgressRepository {

    fun observeLoading(): LiveData<LoadingState>

}