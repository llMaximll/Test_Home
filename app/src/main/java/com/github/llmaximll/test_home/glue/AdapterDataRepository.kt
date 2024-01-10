package com.github.llmaximll.test_home.glue

import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.core.common.models.Camera
import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.data.DataRepositoryImpl
import javax.inject.Inject

class AdapterDataRepository @Inject constructor(
    private val dataRepository: DataRepositoryImpl
) : DataRepository {

    override suspend fun getCameras(): Result<Camera> {
        return dataRepository.getCameras()
    }

    override suspend fun getDoors(): Result<Door> {
        return dataRepository.getDoors()
    }
}