package com.github.llmaximll.test_home.data.remote.models

import androidx.annotation.Keep
import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.core.common.models.Room
import com.google.gson.annotations.SerializedName

@Keep
data class DoorDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("room")
    val room: Room?,
    @SerializedName("favorites")
    val favorites: Boolean?,
    @SerializedName("snapshot")
    val snapshot: String?
)

internal fun DoorDto.asModel(): Door? {
    return Door(
        id = this.id ?: return null,
        name = this.name ?: return null,
        snapshot = this.snapshot ?: return null,
        room = this.room ?: Room.UNDEFINED,
        favorites = this.favorites ?: false
    )
}