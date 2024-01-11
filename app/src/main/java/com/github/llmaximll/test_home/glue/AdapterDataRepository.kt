package com.github.llmaximll.test_home.glue

import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.data.DataRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdapterDataRepository @Inject constructor(
    private val dataRepository: DataRepositoryImpl
) : DataRepository {

    override suspend fun getAllCamerasFlow(): Flow<List<CameraDetails>> =
        dataRepository.getAllCamerasFlow()

    override suspend fun getAllDoorsFlow(): Flow<List<Door>> =
        dataRepository.getAllDoorsFlow()

    override suspend fun updateCameraLocal(newCamera: CameraDetails) {
        dataRepository.updateCameraLocal(newCamera)
    }

    override suspend fun updateDoorLocal(newDoor: Door) {
        dataRepository.updateDoorLocal(newDoor)
    }

    override suspend fun deleteAllCamerasLocal() {
        dataRepository.deleteAllCamerasLocal()
    }

    override suspend fun deleteAllDoorsLocal() {
        dataRepository.deleteAllDoorsLocal()
    }

    override suspend fun downloadAllCameras() {
        dataRepository.downloadAllCameras()
    }

    override suspend fun downloadAllDoors() {
        dataRepository.downloadAllDoors()
    }
}