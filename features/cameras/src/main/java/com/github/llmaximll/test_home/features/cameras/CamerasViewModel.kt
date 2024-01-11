package com.github.llmaximll.test_home.features.cameras

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.core.common.err
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CamerasViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val mutableCameras = MutableLiveData<CamerasUiState>(CamerasUiState.Init)
    val cameras: LiveData<CamerasUiState>
        get() = mutableCameras

    private var job: Job? = null

    init {
        getCameras()
    }

    private fun getCameras() {
        job?.cancel()
        job = viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                err(exception.message.toString())
                mutableCameras.value = CamerasUiState.Error(exception)
            }
        ) {
            mutableCameras.value = CamerasUiState.Loading

            dataRepository.getAllCamerasFlow().collectLatest { list ->
                if (list.isNotEmpty()) {
                    mutableCameras.value = CamerasUiState.Success(list.sortedBy { it.room.ordinal })
                } else {
                    mutableCameras.value = CamerasUiState.Error()
                }
            }
        }
    }

    fun toggleFavourite(camera: CameraDetails) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                err(exception.message.toString())
                mutableCameras.value = CamerasUiState.Error(exception)
            }
        ) {
            dataRepository.updateCameraLocal(camera.copy(favorites = !camera.favorites))
        }
    }

    fun refresh() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                err(exception.message.toString())
                mutableCameras.value = CamerasUiState.Error(exception)
            }
        ) {
            dataRepository.deleteAllCamerasLocal()
            getCameras()
        }
    }
}

sealed class CamerasUiState {

    data object Init : CamerasUiState()

    data object Loading : CamerasUiState()

    data class Success(val cameras: List<CameraDetails>) : CamerasUiState()

    data class Error(val e: Throwable? = null) : CamerasUiState()
}