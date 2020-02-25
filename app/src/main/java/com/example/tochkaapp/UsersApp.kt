package com.example.tochkaapp

import android.app.Application
import android.util.Log
import com.example.tochkaapp.data.di.DaggerDataComponent
import com.example.tochkaapp.data.di.DataComponent
import com.example.tochkaapp.data.di.DataModule
import io.reactivex.plugins.RxJavaPlugins

/**
 * Created by Vladimir Kraev
 */
class UsersApp : Application() {

    private lateinit var dataComponent: DataComponent

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable -> Log.e(this@UsersApp.javaClass.simpleName, "Rx: undelivered throwable", throwable) }
        instance = this
        dataComponent = DaggerDataComponent.builder()
            .dataModule(DataModule())
            .build()
    }

    fun getDataComponent() = dataComponent

    companion object {
        private const val TAG = "USERS_APP"
        lateinit var instance: UsersApp
            private set
    }

}


