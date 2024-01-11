package com.github.llmaximll.test_home.data.local.di

import com.github.llmaximll.test_home.data.local.sources.LocalDataSource
import com.github.llmaximll.test_home.data.local.sources.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModule {

    @Binds
    fun bindLocalDataSource(
        impl: LocalDataSourceImpl
    ): LocalDataSource
}