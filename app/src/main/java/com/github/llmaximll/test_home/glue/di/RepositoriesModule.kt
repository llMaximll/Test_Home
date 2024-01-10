package com.github.llmaximll.test_home.glue.di

import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.glue.AdapterDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindDataRepository(
        dataRepository: AdapterDataRepository
    ): DataRepository
}