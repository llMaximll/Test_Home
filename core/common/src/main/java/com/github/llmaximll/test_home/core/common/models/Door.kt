package com.github.llmaximll.test_home.core.common.models

import androidx.annotation.Keep

@Keep
data class Door(
    val id: Int,
    val name: String,
    val room: Room,
    val favorites: Boolean,
    val snapshot: String
)
