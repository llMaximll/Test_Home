package com.github.llmaximll.test_home.data.fake

import com.github.llmaximll.test_home.core.common.models.Camera
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Room
import java.util.UUID

internal val fakeCameras = Camera(
    rooms = listOf(Room.FIRST, Room.SECOND),
    cameras = listOf(
        CameraDetails(
            localId = UUID.randomUUID().toString(),
            remoteId = 1,
            name = "Camera 1",
            snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png",
            room = Room.FIRST,
            favorites = true,
            rec = false
        ),
        CameraDetails(
            localId = UUID.randomUUID().toString(),
            remoteId = 3,
            name = "Camera 2",
            snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png",
            room = Room.UNDEFINED,
            favorites = true,
            rec = false
        ),
        CameraDetails(
            localId = UUID.randomUUID().toString(),
            remoteId = 2,
            name = "Camera 45",
            snapshot = null,
            room = Room.SECOND,
            favorites = false,
            rec = true
        ),
        CameraDetails(
            localId = UUID.randomUUID().toString(),
            remoteId = 6,
            name = "Camera 89",
            snapshot = "https://serverspace.ru/wp-content/uploads/2019/06/backup-i-snapshot.png",
            room = Room.FIRST,
            favorites = true,
            rec = false
        )
    )
)