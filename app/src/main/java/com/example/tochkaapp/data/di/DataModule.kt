package com.example.tochkaapp.data.di

import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.http.api.GithubApiCreator
import com.example.tochkaapp.data.mapper.GithubUserMapper
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.repository.GitHubUsersRepository
import com.example.tochkaapp.data.repository.UsersRepository
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

/**
 * Created by Vladimir Kraev
 */
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideApiCreator(): GithubApiCreator = GithubApiCreator()

    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper = GithubUserMapper()

    @Provides
    @Singleton
    fun provideGithubApi(apiCreator: GithubApiCreator): GithubApi = apiCreator.getApi()

    @Provides
    @Singleton
    fun provideRepository(api: GithubApi, mapper: UserMapper): UsersRepository =
        GitHubUsersRepository(CompositeDisposable(), api, mapper)

}