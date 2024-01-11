package com.github.llmaximll.test_home.features.doors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.llmaximll.test_home.core.common.DataRepository
import com.github.llmaximll.test_home.core.common.err
import com.github.llmaximll.test_home.core.common.models.Door
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoorsViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private val mutableDoors = MutableLiveData<DoorsUiState>(DoorsUiState.Init)
    val doors: LiveData<DoorsUiState> = mutableDoors

    private var job: Job? = null

    init {
        getDoors()
    }

    private fun getDoors() {
        job?.cancel()
        job = viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                err(exception.message.toString())
                mutableDoors.value = DoorsUiState.Error(exception)
            }
        ) {
            mutableDoors.value = DoorsUiState.Loading

            dataRepository.getAllDoorsFlow().collectLatest { list ->
                if (list.isNotEmpty()) {
                    mutableDoors.value = DoorsUiState.Success(list.sortedBy { it.room.ordinal })
                } else {
                    mutableDoors.value = DoorsUiState.Error()
                }
            }
        }
    }

    fun toggleFavourite(door: Door) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                err(exception.message.toString())
                mutableDoors.value = DoorsUiState.Error(exception)
            }
        ) {
            dataRepository.updateDoorLocal(door.copy(favorites = !door.favorites))
        }
    }

    fun editName(door: Door, newName: String) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                err(exception.message.toString())
                mutableDoors.value = DoorsUiState.Error(exception)
            }
        ) {
            dataRepository.updateDoorLocal(door.copy(name = newName))
        }
    }

    fun refresh() {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, exception ->
                err(exception.message.toString())
                mutableDoors.value = DoorsUiState.Error(exception)
            }
        ) {
            dataRepository.deleteAllDoorsLocal()
            getDoors()
        }
    }
}

sealed class DoorsUiState {

    data object Init : DoorsUiState()

    data object Loading : DoorsUiState()

    data class Success(val doors: List<Door>) : DoorsUiState()

    data class Error(val e: Throwable? = null) : DoorsUiState()
}