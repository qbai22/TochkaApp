package com.example.tochkaapp.data.di
import com.example.tochkaapp.screen.users.UsersListViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Vladimir Kraev
 */

@Singleton
@Component(modules = [DataModule::class])
interface DataComponent {

    fun inject(usersListViewModel: UsersListViewModel)

}