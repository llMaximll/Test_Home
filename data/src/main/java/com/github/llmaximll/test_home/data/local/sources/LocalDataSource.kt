package com.github.llmaximll.test_home.data.local.sources

import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.data.local.models.CameraDetailsEntity
import com.github.llmaximll.test_home.data.local.models.DoorEntity
import com.github.llmaximll.test_home.data.local.models.asModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.delete
import io.realm.kotlin.executeTransactionAwait
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

interface LocalDataSource {

    suspend fun getAllCameras(): List<CameraDetails>

    fun getAllCamerasFlow(): Flow<List<CameraDetails>>

    suspend fun getAllDoors(): List<Door>

    fun getAllDoorsFlow(): Flow<List<Door>>

    suspend fun insertCameras(cameras: List<CameraDetailsEntity>)

    suspend fun insertDoors(doors: List<DoorEntity>)

    suspend fun updateCamera(newCamera: CameraDetailsEntity)

    suspend fun updateDoor(newDoor: DoorEntity)

    suspend fun deleteAllCameras()

    suspend fun deleteAllDoors()
}

class LocalDataSourceImpl @Inject constructor(
    private val realmConfiguration: RealmConfiguration
) : LocalDataSource {

    override suspend fun getAllCameras(): List<CameraDetails> {
        val realm = Realm.getInstance(realmConfiguration)
        val result = mutableListOf<CameraDetails>()

        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            result.addAll(
                transaction
                    .where<CameraDetailsEntity>()
                    .findAll()
                    .mapNotNull {
                        it.asModel()
                    }
            )
        }

        return result
    }

    override fun getAllCamerasFlow(): Flow<List<CameraDetails>> {
        val realm = Realm.getInstance(realmConfiguration)

        return realm
            .where<CameraDetailsEntity>()
            .findAll()
            .toFlow()
            .mapNotNull { results ->
                results.mapNotNull {
                    it.asModel()
                }
            }
    }

    override suspend fun getAllDoors(): List<Door> {
        val realm = Realm.getInstance(realmConfiguration)
        val result = mutableListOf<Door>()

        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            result.addAll(
                transaction
                    .where(DoorEntity::class.java)
                    .findAll()
                    .mapNotNull {
                        it.asModel()
                    }
            )
        }

        return result
    }

    override fun getAllDoorsFlow(): Flow<List<Door>> {
        val realm = Realm.getInstance(realmConfiguration)

        return realm
            .where<DoorEntity>()
            .findAll()
            .toFlow()
            .mapNotNull { results ->
                results.mapNotNull {
                    it.asModel()
                }
            }
    }

    override suspend fun insertCameras(cameras: List<CameraDetailsEntity>) {
        val realm = Realm.getInstance(realmConfiguration)

        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            transaction.insert(cameras)
        }
    }

    override suspend fun insertDoors(doors: List<DoorEntity>) {
        val realm = Realm.getInstance(realmConfiguration)

        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            transaction.insert(doors)
        }
    }

    override suspend fun updateCamera(newCamera: CameraDetailsEntity) {
        val realm = Realm.getInstance(realmConfiguration)

        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            transaction.copyToRealmOrUpdate(newCamera)
        }
    }

    override suspend fun updateDoor(newDoor: DoorEntity) {
        val realm = Realm.getInstance(realmConfiguration)

        realm.executeTransactionAwait(Dispatchers.IO) { transaction ->
            transaction.copyToRealmOrUpdate(newDoor)
        }
    }

    override suspend fun deleteAllCameras() {
        val realm = Realm.getInstance(realmConfiguration)

        realm.executeTransactionAwait { transaction ->
            transaction.delete<CameraDetailsEntity>()
        }
    }

    override suspend fun deleteAllDoors() {
        val realm = Realm.getInstance(realmConfiguration)

        realm.executeTransactionAwait { transaction ->
            transaction.delete<DoorEntity>()
        }
    }
}