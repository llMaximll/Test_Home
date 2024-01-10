package com.github.llmaximll.test_home.data.remote.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RequestResult<out T>(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("data")
    val data: T
)
