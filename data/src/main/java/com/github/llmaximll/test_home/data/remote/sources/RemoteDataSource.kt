package com.github.llmaximll.test_home.data.remote.sources

import com.github.llmaximll.test_home.core.common.err
import com.github.llmaximll.test_home.core.common.log
import com.github.llmaximll.test_home.data.remote.models.CameraDto
import com.github.llmaximll.test_home.data.remote.models.DoorDto
import com.github.llmaximll.test_home.data.remote.models.RequestResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.invoke
import io.ktor.client.request.request
import io.ktor.http.Url
import io.ktor.util.KtorDsl
import javax.inject.Inject

interface RemoteDataSource {

    suspend fun getCameras(): RequestResult<CameraDto>?

    suspend fun getDoors(): RequestResult<DoorDto>?
}

class RemoteDataSourceImpl @Inject constructor(
    private val client: HttpClient
) : RemoteDataSource {

    override suspend fun getCameras(): RequestResult<CameraDto>? {
        val result = try {
            val response = client.get("http://cars.cprogroup.ru/api/rubetek/cameras/")
            response.body<RequestResult<CameraDto>>()
        } catch (e: Exception) {
            err(e)
            null
        }

        return result
    }

    override suspend fun getDoors(): RequestResult<DoorDto>? {
        return try {
            val response = client.get("http://cars.cprogroup.ru/api/rubetek/doors/")
            response.body<RequestResult<DoorDto>>()
        } catch (e: Exception) {
            err(e)
            null
        }
    }
}