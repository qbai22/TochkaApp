package com.example.tochkaapp.data.repository.source

import androidx.paging.PositionalDataSource
import com.example.tochkaapp.data.repository.Closable
import com.example.tochkaapp.data.repository.Loadable

/**
 * Created by Vladimir Kraev
 */
abstract class RepeatableDataSource<T> : PositionalDataSource<T>(),
    Loadable, Closable {

    abstract fun repeatLastCall()

}