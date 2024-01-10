package com.github.llmaximll.test_home.features.cameras

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.llmaximll.test_home.core.common.log

@Composable
fun CamerasScreen(
    modifier: Modifier = Modifier,
    viewModel: CamerasViewModel = hiltViewModel()
) {
    Box( // TODO("Заменить")
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        val camerasState by viewModel.cameras.observeAsState()

        LaunchedEffect(camerasState) {
            log("camerasState: $camerasState")
        }
    }
}