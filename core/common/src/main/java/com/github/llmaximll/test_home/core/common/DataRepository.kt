package com.github.llmaximll.test_home.core.common

import com.github.llmaximll.test_home.core.common.models.Camera
import com.github.llmaximll.test_home.core.common.models.Door

interface DataRepository {

    suspend fun getCameras(): Result<Camera>

    suspend fun getDoors(): Result<Door>
}