package com.github.llmaximll.test_home.data.remote.di

import com.github.llmaximll.test_home.data.remote.sources.RemoteDataSource
import com.github.llmaximll.test_home.data.remote.sources.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModule {

    @Binds
    fun bindRemoteDataSource(
        impl: RemoteDataSourceImpl
    ): RemoteDataSource
}