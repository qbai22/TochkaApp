package com.example.tochkaapp.data.di

import com.example.tochkaapp.data.http.api.GithubApi
import com.example.tochkaapp.data.http.api.GithubApiCreator
import com.example.tochkaapp.data.mapper.GithubUserMapper
import com.example.tochkaapp.data.mapper.UserMapper
import com.example.tochkaapp.data.repository.GitHubUsersRepository
import com.example.tochkaapp.data.repository.UsersRepository
import com.example.tochkaapp.data.repository.factory.GithubUsersDataSourceFactory
import com.example.tochkaapp.data.repository.factory.UsersDataSourcesFactory
import dagger.Module
import dagger.Provides
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
    fun provideUsersDataSourceFactory(api: GithubApi, mapper: UserMapper): UsersDataSourcesFactory =
        GithubUsersDataSourceFactory(api, mapper)

    @Provides
    @Singleton
    fun provideRepository(usersDataSourceFactory: UsersDataSourcesFactory): UsersRepository =
        GitHubUsersRepository(usersDataSourceFactory)


}