package com.github.llmaximll.test_home.data.local.models

import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Room.Companion.asRoom
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class CameraDetailsEntity(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    var remoteId: Int = -1,
    @Required
    var name: String = "",
    var snapshot: String? = null,
    @Required
    var room: String = "",
    var favorites: Boolean = false,
    var rec: Boolean = false
) : RealmObject()

internal fun CameraDetailsEntity.asModel(): CameraDetails? {
    return CameraDetails(
        localId = this.id,
        remoteId = this.remoteId,
        name = this.name,
        snapshot = this.snapshot ?: return null,
        room = this.room.asRoom(),
        favorites = this.favorites,
        rec = this.rec
    )
}

internal fun CameraDetails.asEntity(): CameraDetailsEntity {
    return CameraDetailsEntity(
        id = this.localId,
        remoteId = this.remoteId,
        name = this.name,
        snapshot = this.snapshot,
        room = this.room.name,
        favorites = this.favorites,
        rec = this.rec
    )
}