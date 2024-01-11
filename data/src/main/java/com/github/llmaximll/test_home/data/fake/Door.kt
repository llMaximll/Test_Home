package com.github.llmaximll.test_home.data.fake

import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.core.common.models.Room
import java.util.UUID

internal val fakeDoors = listOf(
    Door(
        localId = UUID.randomUUID().toString(),
        remoteId = 1,
        name = "Door Door",
        room = Room.FIRST,
        favorites = true,
        snapshot = null
    ),
    Door(
        localId = UUID.randomUUID().toString(),
        remoteId = 3,
        name = "Door 45",
        room = Room.UNDEFINED,
        favorites = true,
        snapshot = null
    ),
    Door(
        localId = UUID.randomUUID().toString(),
        remoteId = 2,
        name = "Door Door Door Door",
        room = Room.UNDEFINED,
        favorites = false,
        snapshot = null
    ),
    Door(
        localId = UUID.randomUUID().toString(),
        remoteId = 6,
        name = "Door Door, Door Door",
        room = Room.FIRST,
        favorites = true,
        snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png"
    )
)