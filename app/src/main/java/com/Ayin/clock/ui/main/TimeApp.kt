package com.ayin.clock.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ayin.clock.ui.clock.ClockScreen
import com.ayin.clock.ui.stopwatch.StopwatchScreen
import com.ayin.clock.ui.theme.ThemeColors
import com.ayin.clock.ui.theme.ThemeSwitcher
import com.ayin.clock.ui.timer.TimerScreen
import com.ayin.clock.ui.timer.TimerViewModel
import com.ayin.clock.ui.stopwatch.StopwatchViewModel
import com.ayin.clock.util.slideAnimation
import kotlin.collections.get

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TimeApp(
    timerViewModel: TimerViewModel,
    stopwatchViewModel: StopwatchViewModel
) {
    var currentThemeIndex by remember { mutableIntStateOf(0) }
    var currentTab by remember { mutableIntStateOf(0) }
    val currentColorScheme = ThemeColors.schemes[currentThemeIndex].second
    val tabs = listOf("时钟", "计时器", "秒表")

    // 创建MaterialTheme，使用当前选中的颜色方案
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = currentColorScheme.primary,
            onPrimary = Color.White,
            secondary = currentColorScheme.secondary,
            onSecondary = Color.Black,
            tertiary = currentColorScheme.tertiary,
            onTertiary = Color.White,
            surface = currentColorScheme.surface,
            onSurface = currentColorScheme.onSurface,
            background = currentColorScheme.surface,
            onBackground = currentColorScheme.onSurface
        )
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "timekeeper",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        ThemeSwitcher(
                            currentThemeIndex = currentThemeIndex,
                            onThemeChange = { currentThemeIndex = it }
                        )
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    tabs.forEachIndexed { index, title ->
                        NavigationBarItem(
                            icon = {
                                val icon: ImageVector = when (index) {
                                    0 -> Icons.Default.AccessTime
                                    1 -> Icons.Default.HourglassTop
                                    2 -> Icons.Default.Timer
                                    else -> Icons.Default.AccessTime
                                }
                                Icon(icon, title)
                            },
                            label = { Text(title) },
                            selected = currentTab == index,
                            onClick = { currentTab = index },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { slideAnimation(if (targetState > initialState) 1 else -1) }
                ) { tab ->
                    when (tab) {
                        0 -> ClockScreen()
                        1 -> TimerScreen(viewModel = timerViewModel)
                        2 -> StopwatchScreen(viewModel = stopwatchViewModel)
                        else -> ClockScreen()
                    }
                }
            }
        }
    }
}