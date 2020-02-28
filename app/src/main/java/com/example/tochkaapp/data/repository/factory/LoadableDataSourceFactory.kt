package com.example.tochkaapp.data.repository.factory

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.tochkaapp.data.repository.source.Loadable
import com.example.tochkaapp.utils.LoadingState

/**
 * Created by Vladimir Kraev
 */

/** Factory class which is publishing [DataSource] which supports [LoadingState] publishing */
abstract class LoadableDataSourceFactory<V, T> : DataSource.Factory<V, T>() {

    abstract val loadable: LiveData<Loadable>

}