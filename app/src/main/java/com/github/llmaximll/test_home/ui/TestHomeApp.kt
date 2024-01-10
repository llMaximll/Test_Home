package com.github.llmaximll.test_home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition

import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.llmaximll.test_home.R
import com.github.llmaximll.test_home.features.cameras.CamerasScreen
import com.github.llmaximll.test_home.features.doors.DoorsScreen
import com.github.llmaximll.test_home.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun TestHomeApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TestHomeTopBar()
        }
    ) { innerPadding ->
        TabsLayout(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestHomeTopBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(id = R.string.app_app_bar_title))
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = AppColors.Background,
            titleContentColor = AppColors.OnBackground
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsLayout(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { 3 }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Tabs(
            pagerState = pagerState
        )

        TabsContent(
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Tabs(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val tabsList = listOf(
        stringResource(id = R.string.app_tabs_cameras),
        stringResource(id = R.string.app_tabs_doors)
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    TabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = pagerState.currentPage,
        containerColor = AppColors.Background,
        contentColor = AppColors.OnBackground,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = 1.dp,
                color = AppColors.TabsIndicator
            )
        }
    ) {
        tabsList.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier,
                unselectedContentColor = AppColors.OnBackground,
                selectedContentColor = AppColors.OnBackground,
                selected = pagerState.currentPage == selectedTabIndex,
                onClick = {
                    coroutineScope.launch {
                        selectedTabIndex = index
                        pagerState.animateScrollToPage(selectedTabIndex)
                    }
                }
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = title,
                    color = AppColors.OnBackground
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsContent(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        when (page) {
            0 -> CamerasScreen()

            1 -> DoorsScreen()
        }
    }
}