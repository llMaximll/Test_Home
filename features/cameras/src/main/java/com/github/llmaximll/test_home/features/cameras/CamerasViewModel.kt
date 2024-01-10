package com.github.llmaximll.test_home.features.cameras

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.core.common.models.Camera
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CamerasViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val mutableCameras = MutableLiveData<CamerasUiState>(CamerasUiState.Init)
    val cameras: LiveData<CamerasUiState>
        get() = mutableCameras

    init {
        getCameras()
    }

    private fun getCameras() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                mutableCameras.value = CamerasUiState.Error(exception)
            }
        ) {
            mutableCameras.value = CamerasUiState.Loading

            val result = dataRepository.getCameras()
            val data = result.getOrNull()

            if (result.isSuccess && data != null) {
                mutableCameras.value = CamerasUiState.Success(data)
            } else {
                mutableCameras.value = CamerasUiState.Error(result.exceptionOrNull())
            }
        }
    }
}

sealed class CamerasUiState {

    data object Init : CamerasUiState()

    data object Loading : CamerasUiState()

    data class Success(val camera: Camera) : CamerasUiState()

    data class Error(val e: Throwable? = null) : CamerasUiState()
}