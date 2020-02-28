package com.example.tochkaapp.data.repository.source

import androidx.lifecycle.MutableLiveData
import com.example.tochkaapp.utils.LoadingState

/**
 * Created by Vladimir Kraev
 */
interface Loadable {

    val loadingState: MutableLiveData<LoadingState>
    val initialLoadingState: MutableLiveData<LoadingState>

}