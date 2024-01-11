package com.github.llmaximll.test_home.core.common.models

import androidx.annotation.Keep

@Keep
data class Door(
    val localId: String,
    val remoteId: Int,
    val name: String,
    val room: Room,
    val favorites: Boolean,
    val snapshot: String?
)
