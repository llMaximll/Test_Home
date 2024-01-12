package com.github.llmaximll.test_home.features.cameras

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.llmaximll.test_home.core.common.compose.CommonText
import com.github.llmaximll.test_home.core.common.compose.rememberShimmerEffect
import com.github.llmaximll.test_home.core.common.log
import com.github.llmaximll.test_home.core.common.models.CameraDetails
import com.github.llmaximll.test_home.core.common.models.Room
import com.github.llmaximll.test_home.core.common.theme.AppColors
import com.kevinnzou.compose.swipebox.SwipeBox
import com.kevinnzou.compose.swipebox.widget.SwipeIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.github.llmaximll.test_home.core.common.R as ResCommon

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CamerasScreen(
    modifier: Modifier = Modifier,
    viewModel: CamerasViewModel = hiltViewModel()
) {
    val camerasState by viewModel.cameras.observeAsState(CamerasUiState.Init)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = camerasState is CamerasUiState.Loading,
        onRefresh = viewModel::refresh
    )

    LaunchedEffect(camerasState) {
        log("camerasState: $camerasState")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.Center
    ) {
        when (val screenState = camerasState) {
            CamerasUiState.Init,
            CamerasUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = AppColors.TabsIndicator
                )
            }

            is CamerasUiState.Error -> {
                CommonText(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = ResCommon.string.core_common_error_network),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            is CamerasUiState.Success -> {
                CamerasList(
                    items = screenState.cameras,
                    onFavourites = viewModel::toggleFavourite
                )
            }
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = camerasState is CamerasUiState.Loading,
            state = pullRefreshState,
            backgroundColor = AppColors.Background,
            contentColor = AppColors.TabsIndicator
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CamerasList(
    items: List<CameraDetails>,
    onFavourites: (camera: CameraDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmerEffect = rememberShimmerEffect()

    var prevRoom by remember { mutableStateOf(Room.FIRST) }

    var currentSwipeState: SwipeableState<Int>? by remember {
        mutableStateOf(null)
    }
    val coroutineScope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (currentSwipeState != null && currentSwipeState!!.currentValue != 0) {
                    coroutineScope.launch {
                        currentSwipeState?.animateTo(0)
                        currentSwipeState = null
                    }
                }
                return Offset.Zero
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        state = rememberLazyListState(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.localId }
        ) { index, item: CameraDetails ->
            if (item.room != prevRoom || index == 0) {
                CommonText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = item.room.titleRes),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            CameraItem(
                cameraDetails = item,
                shimmerEffect = shimmerEffect,
                currentSwipeState = currentSwipeState,
                coroutineScope = coroutineScope,
                onCurrentSwipeStateModify = {
                    currentSwipeState = it
                },
                onFavourites = onFavourites
            )

            prevRoom = item.room
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CameraItem(
    cameraDetails: CameraDetails,
    shimmerEffect: Color,
    currentSwipeState: SwipeableState<Int>?,
    coroutineScope: CoroutineScope,
    onCurrentSwipeStateModify: (SwipeableState<Int>?) -> Unit,
    onFavourites: (camera: CameraDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    val onSwipeStateChanged = { state: SwipeableState<Int> ->
        if (state.targetValue == 0 && currentSwipeState == state) {
            onCurrentSwipeStateModify(null)
        } else if (currentSwipeState == null) {
            onCurrentSwipeStateModify(state)
        } else {
            coroutineScope.launch {
                currentSwipeState.animateTo(0)
                onCurrentSwipeStateModify(state)
            }
        }
    }

    SwipeBox(
        modifier = modifier.fillMaxWidth(),
        endContentWidth = 60.dp,
        endContent = { swipeState, _ ->
            SwipeIcon(
                painter = painterResource(id = ResCommon.drawable.favourites_dismiss),
                contentDescription = null,
                tint = Color.Unspecified,
                background = AppColors.Background,
                weight = 1f,
                iconSize = 44.dp
            ) {
                coroutineScope.launch {
                    swipeState.animateTo(0)
                }

                onFavourites(cameraDetails)
            }
        }
    ) { state, _, _ ->
        CameraItemContent(
            modifier = Modifier.padding(6.dp),
            cameraDetails = cameraDetails,
            shimmerEffect = shimmerEffect
        )

        LaunchedEffect(state.targetValue) {
            onSwipeStateChanged(state)
        }
    }
}

@Composable
private fun CameraItemContent(
    cameraDetails: CameraDetails,
    shimmerEffect: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = AppColors.Background,
            contentColor = AppColors.OnBackground
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (cameraDetails.snapshot != null) {
                    Box(
                        modifier = Modifier
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(shimmerEffect),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(cameraDetails.snapshot)
                                .crossfade(true)
                                .build(),
                            contentDescription = cameraDetails.name,
                            contentScale = ContentScale.FillWidth
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = .4f))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    painter = painterResource(id = ResCommon.drawable.play_button),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ) {
                            AnimatedVisibility(cameraDetails.favorites) {
                                Icon(
                                    modifier = Modifier
                                        .size(24.dp),
                                    painter = painterResource(id = ResCommon.drawable.favourites),
                                    contentDescription = null,
                                    tint = Color.Yellow
                                )

                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AnimatedContent(
                        targetState = cameraDetails.name,
                        label = "Name"
                    ) { name ->
                        CommonText(
                            text = name
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row {
                        AnimatedVisibility(cameraDetails.favorites && cameraDetails.snapshot == null) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(id = ResCommon.drawable.favourites),
                                contentDescription = null,
                                tint = Color.Yellow
                            )

                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }
        }
    }
}