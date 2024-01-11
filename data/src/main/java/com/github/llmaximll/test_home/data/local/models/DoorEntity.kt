package com.github.llmaximll.test_home.data.local.models

import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.core.common.models.Room.Companion.asRoom
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId

open class DoorEntity(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    var remoteId: Int = -1,
    @Required
    var name: String = "",
    @Required
    var room: String = "",
    var favorites: Boolean = false,
    var snapshot: String? = null
) : RealmObject()

internal fun DoorEntity.asModel(): Door {
    return Door(
        localId = this.id,
        remoteId = this.remoteId,
        name = this.name,
        snapshot = this.snapshot,
        room = this.room.asRoom(),
        favorites = this.favorites
    )
}

internal fun Door.asEntity(): DoorEntity {
    return DoorEntity(
        id = this.localId,
        remoteId = this.remoteId,
        name = this.name,
        snapshot = this.snapshot,
        room = this.room.name,
        favorites = this.favorites
    )
}