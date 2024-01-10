package com.github.llmaximll.test_home.data

import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.core.common.log
import com.github.llmaximll.test_home.core.common.models.Camera
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.core.common.models.Room
import com.github.llmaximll.test_home.data.remote.models.RequestResult
import com.github.llmaximll.test_home.data.remote.models.asModel
import com.github.llmaximll.test_home.data.remote.sources.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : DataRepository {

    override suspend fun getCameras(): Result<Camera> {
//        val result = remoteDataSource.getCameras() TODO
        val data = Camera(rooms=listOf(Room.FIRST, Room.SECOND), cameras=listOf(CameraDetails(id=1, name="Camera 1", snapshot="https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png", room=Room.FIRST, favorites=true, rec=false), CameraDetails(id=3, name="Camera 2", snapshot="https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png", room=Room.UNDEFINED, favorites=true, rec=false), CameraDetails(id=2, name="Camera 45", snapshot="https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png", room=Room.FIRST, favorites=false, rec=true), CameraDetails(id=6, name="Camera 89", snapshot="https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png", room=Room.FIRST, favorites=true, rec=false)))
//        val data = result?.data?.asModel()

        log("getCameras:: data: $data")

        return if (/*result?.success == true && */data != null) {
            Result.success(data)
        } else {
            Result.failure(NullPointerException())
        }
    }

    override suspend fun getDoors(): Result<Door> {
        val result = remoteDataSource.getDoors()
        val data = result?.data?.asModel()

        return if (result?.success == true && data != null) {
            Result.success(data)
        } else {
            Result.failure(NullPointerException())
        }
    }
}