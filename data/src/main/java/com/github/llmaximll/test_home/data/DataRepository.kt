package com.github.llmaximll.test_home.data

import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.core.common.log
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.data.local.models.asEntity
import com.github.llmaximll.test_home.data.local.sources.LocalDataSource
import com.github.llmaximll.test_home.data.remote.models.asModel
import com.github.llmaximll.test_home.data.remote.sources.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : DataRepository {

    override suspend fun getAllCamerasFlow(): Flow<List<CameraDetails>> = flow {
        val localCamerasFlow = localDataSource.getAllCamerasFlow()

        if (!localCamerasFlow.firstOrNull().isNullOrEmpty()) {
            log(
                """getCameras:: Local
                |localCameras: ${localCamerasFlow.firstOrNull()}
            """.trimMargin()
            )
            emitAll(localCamerasFlow)
        } else {
            val remoteResult = remoteDataSource.getCameras()
            val remoteCameras = remoteResult?.data?.asModel()?.cameras

            if (!remoteCameras.isNullOrEmpty()) {
                log(
                    """getCameras:: Remote
                |remoteCameras: $remoteCameras
            """.trimMargin()
                )

                localDataSource.insertCameras(remoteCameras.map { it.asEntity() })

                emitAll(localCamerasFlow)
            } else {
                log("getCameras:: Failure")
            }
        }
    }

    override suspend fun getAllDoorsFlow(): Flow<List<Door>> = flow {
        val localDoorsFlow = localDataSource.getAllDoorsFlow()

        if (!localDoorsFlow.firstOrNull().isNullOrEmpty()) {
            log(
                """getDoors:: Local
                |localDoors: ${localDoorsFlow.firstOrNull()}
            """.trimMargin()
            )
            emitAll(localDoorsFlow)
        } else {
            val remoteResult = remoteDataSource.getDoors()
            val remoteDoors = remoteResult?.data?.mapNotNull { it.asModel() }

            if (!remoteDoors.isNullOrEmpty()) {
                log(
                    """getDoors:: Remote
                |remoteDoors: $remoteDoors
            """.trimMargin()
                )

                localDataSource.insertDoors(remoteDoors.map { it.asEntity() })

                emitAll(localDoorsFlow)
            } else {
                log("getDoors:: Failure")
            }
        }
    }

    override suspend fun updateCameraLocal(newCamera: CameraDetails) {
        localDataSource.updateCamera(newCamera.asEntity())
    }

    override suspend fun updateDoorLocal(newDoor: Door) {
        localDataSource.updateDoor(newDoor.asEntity())
    }

    override suspend fun deleteAllCamerasLocal() {
        localDataSource.deleteAllCameras()
    }

    override suspend fun deleteAllDoorsLocal() {
        localDataSource.deleteAllDoors()
    }

    override suspend fun downloadAllCameras() {
        val remoteResult = remoteDataSource.getCameras()
        val remoteCameras = remoteResult?.data?.asModel()?.cameras

        if (!remoteCameras.isNullOrEmpty()) {
            log(
                """downloadAllCameras:: Remote
                |remoteCameras: $remoteCameras
            """.trimMargin()
            )

            localDataSource.insertCameras(remoteCameras.map { it.asEntity() })
        } else {
            log("downloadAllCameras:: Failure")
        }
    }

    override suspend fun downloadAllDoors() {
        val remoteResult = remoteDataSource.getDoors()
        val remoteDoors = remoteResult?.data?.mapNotNull { it.asModel() }

        if (!remoteDoors.isNullOrEmpty()) {
            log(
                """downloadAllDoors:: Remote
                |remoteCameras: $remoteDoors
            """.trimMargin()
            )

            localDataSource.insertDoors(remoteDoors.map { it.asEntity() })
        } else {
            log("downloadAllDoors:: Failure")
        }
    }
}