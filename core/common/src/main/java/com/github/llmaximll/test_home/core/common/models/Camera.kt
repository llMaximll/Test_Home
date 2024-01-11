package com.github.llmaximll.test_home.core.common.models

import androidx.annotation.Keep

@Keep
data class Camera(
    val rooms: List<Room>,
    val cameras: List<CameraDetails>
)

@Keep
data class CameraDetails(
    val localId: String,
    val remoteId: Int,
    val name: String,
    val snapshot: String,
    val room: Room,
    val favorites: Boolean,
    val rec: Boolean
)
