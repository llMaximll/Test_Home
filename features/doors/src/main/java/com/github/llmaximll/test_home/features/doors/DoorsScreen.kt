package com.github.llmaximll.test_home.features.doors

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDismissState
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
import com.github.llmaximll.test_home.core.common.models.Door
import com.github.llmaximll.test_home.core.common.models.Room
import com.github.llmaximll.test_home.core.common.theme.AppColors
import com.kevinnzou.compose.swipebox.SwipeBox
import com.kevinnzou.compose.swipebox.widget.SwipeIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.github.llmaximll.test_home.core.common.R as ResCommon

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorsScreen(
    modifier: Modifier = Modifier,
    viewModel: DoorsViewModel = hiltViewModel()
) {
    val doorsState by viewModel.doors.observeAsState(DoorsUiState.Init)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = doorsState is DoorsUiState.Loading,
        onRefresh = viewModel::refresh
    )

    var editNameDialogProperties by remember { mutableStateOf<Pair<Boolean, Door?>>(false to null) }

    LaunchedEffect(doorsState) {
        log("doorsState: $doorsState")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.Center
    ) {
        when (val screenState = doorsState) {
            DoorsUiState.Init,
            DoorsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = AppColors.TabsIndicator
                )
            }

            is DoorsUiState.Error -> {
                CommonText(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = ResCommon.string.core_common_error_network),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }

            is DoorsUiState.Success -> {
                DoorsList(
                    items = screenState.doors,
                    onFavourites = viewModel::toggleFavourite,
                    onEdit = {
                        editNameDialogProperties = true to it
                    }
                )
            }
        }

        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = doorsState is DoorsUiState.Loading,
            state = pullRefreshState,
            backgroundColor = AppColors.Background,
            contentColor = AppColors.TabsIndicator
        )

        EditNameDialog(
            editNameDialogProperties = editNameDialogProperties,
            onPositive = { newName: String, door: Door ->
                viewModel.editName(door, newName)
                editNameDialogProperties = false to null
            },
            onDismiss = { editNameDialogProperties = false to null }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DoorsList(
    items: List<Door>,
    onFavourites: (door: Door) -> Unit,
    onEdit: (door: Door) -> Unit,
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
        ) { _, door: Door ->
            DoorItem(
                door = door,
                shimmerEffect = shimmerEffect,
                currentSwipeState = currentSwipeState,
                coroutineScope = coroutineScope,
                onCurrentSwipeStateModify = {
                    currentSwipeState = it
                },
                onFavourites = onFavourites,
                onEdit = onEdit
            )

            prevRoom = door.room
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DoorItem(
    door: Door,
    shimmerEffect: Color,
    currentSwipeState: SwipeableState<Int>?,
    coroutineScope: CoroutineScope,
    onCurrentSwipeStateModify: (SwipeableState<Int>?) -> Unit,
    onFavourites: (door: Door) -> Unit,
    onEdit: (door: Door) -> Unit,
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
        endContentWidth = 120.dp,
        endContent = { swipeState, _ ->
            SwipeIcon(
                painter = painterResource(id = ResCommon.drawable.edit_dismiss),
                contentDescription = null,
                tint = AppColors.TabsIndicator,
                background = AppColors.Background,
                weight = 1f,
                iconSize = 44.dp
            ) {
                coroutineScope.launch {
                    swipeState.animateTo(0)
                }

                onEdit(door)
            }

            SwipeIcon(
                painter = painterResource(id = ResCommon.drawable.favourites_dismiss),
                contentDescription = null,
                tint = Color.Yellow,
                background = AppColors.Background,
                weight = 1f,
                iconSize = 44.dp
            ) {
                coroutineScope.launch {
                    swipeState.animateTo(0)
                }

                onFavourites(door)
            }
        }
    ) { state, _, _ ->
        DoorItemContent(
            modifier = Modifier.padding(6.dp),
            door = door,
            shimmerEffect = shimmerEffect
        )

        LaunchedEffect(state.targetValue) {
            onSwipeStateChanged(state)
        }
    }
}

@Composable
private fun DoorItemContent(
    door: Door,
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
                if (door.snapshot != null) {
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
                                .data(door.snapshot)
                                .crossfade(true)
                                .build(),
                            contentDescription = door.name,
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
                        targetState = door.name,
                        label = "Name"
                    ) { name ->
                        CommonText(
                            text = name
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row {
                        AnimatedVisibility(door.favorites) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp),
                                painter = painterResource(id = ResCommon.drawable.favourites),
                                contentDescription = null,
                                tint = Color.Yellow
                            )

                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = ResCommon.drawable.lock),
                            contentDescription = null,
                            tint = AppColors.TabsIndicator
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoorItemDismissBackground(
    dismissState: DismissState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
            Icon(
                modifier = Modifier.size(44.dp),
                painter = painterResource(id = ResCommon.drawable.favourites_dismiss),
                contentDescription = null,
                tint = Color.Yellow
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (dismissState.dismissDirection == DismissDirection.EndToStart) {
            Icon(
                modifier = Modifier.size(44.dp),
                painter = painterResource(id = ResCommon.drawable.edit_dismiss),
                contentDescription = null,
                tint = AppColors.TabsIndicator
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditNameDialog(
    editNameDialogProperties: Pair<Boolean, Door?>,
    onPositive: (String, Door) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (editNameDialogProperties.first) {
        var newName by remember { mutableStateOf("") }

        AlertDialog(
            modifier = modifier,
            onDismissRequest = { onDismiss() }
        ) {
            ElevatedCard(
                modifier = Modifier,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = AppColors.Background,
                    contentColor = AppColors.OnBackground
                ),
                onClick = { }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = newName,
                        onValueChange = { newName = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White
                            ),
                            onClick = {
                                onDismiss()
                            }
                        ) {
                            CommonText(
                                text = stringResource(id = ResCommon.string.core_common_dialog_save_neutral),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White
                            ),
                            onClick = {
                                if (newName.isNotEmpty() && editNameDialogProperties.second != null)
                                    onPositive(newName, editNameDialogProperties.second!!)
                            }
                        ) {
                            Text(
                                text = stringResource(id = ResCommon.string.core_common_dialog_save_positive),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}