package com.github.llmaximll.test_home.core.common

import com.github.llmaximll.test_home.core.common.models.Camera
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Door
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    suspend fun getAllCamerasFlow(): Flow<List<CameraDetails>>

    suspend fun getAllDoorsFlow(): Flow<List<Door>>

    suspend fun updateCameraLocal(newCamera: CameraDetails)

    suspend fun updateDoorLocal(newDoor: Door)

    suspend fun deleteAllCamerasLocal()

    suspend fun deleteAllDoorsLocal()

    suspend fun downloadAllCameras()

    suspend fun downloadAllDoors()
}