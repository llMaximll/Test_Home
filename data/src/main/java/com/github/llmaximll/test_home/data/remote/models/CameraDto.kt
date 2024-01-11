package com.github.llmaximll.test_home.data.remote.models

import androidx.annotation.Keep
import com.github.llmaximll.test_home.core.common.models.Camera
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Room
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Keep
data class CameraDto(
    @SerializedName("room")
    val room: List<Room>?,
    @SerializedName("cameras")
    val cameras: List<CameraDetailsDto>?
)

@Keep
data class CameraDetailsDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("snapshot")
    val snapshot: String?,
    @SerializedName("room")
    val room: Room?,
    @SerializedName("favorites")
    val favorites: Boolean?,
    @SerializedName("rec")
    val rec: Boolean?
)

internal fun CameraDto.asModel(): Camera? {
    return Camera(
        rooms = this.room ?: return null,
        cameras = this.cameras?.mapNotNull { it.asModel() } ?: return null
    )
}

private fun CameraDetailsDto.asModel(): CameraDetails? {
    return CameraDetails(
        localId = UUID.randomUUID().toString(),
        remoteId = this.id ?: return null,
        name = this.name ?: return null,
        snapshot = this.snapshot ?: return null,
        room = this.room ?: Room.UNDEFINED,
        favorites = this.favorites ?: false,
        rec = this.rec ?: false
    )
}