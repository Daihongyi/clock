// MainActivity.kt
package com.Ayin.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// 定义不同的配色方案
val PurpleColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFFE8DEF8),
    tertiary = Color(0xFF7D5260),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F)
)

val BlueColorScheme = lightColorScheme(
    primary = Color(0xFF006C84),
    secondary = Color(0xFFB3E5FC),
    tertiary = Color(0xFF006A6A),
    surface = Color(0xFFF7F9FC),
    onSurface = Color(0xFF1A202C)
)

val GreenColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    secondary = Color(0xFFC8E6C9),
    tertiary = Color(0xFF006C4F),
    surface = Color(0xFFF1F8E9),
    onSurface = Color(0xFF1B1B1B)
)

val OrangeColorScheme = lightColorScheme(
    primary = Color(0xFFE65100),
    secondary = Color(0xFFFFCC80),
    tertiary = Color(0xFF7B5E3F),
    surface = Color(0xFFFFF3E0),
    onSurface = Color(0xFF1C1B1B)
)

// 所有可用的配色方案
val colorSchemes = listOf(
    "Purple" to PurpleColorScheme,
    "Blue" to BlueColorScheme,
    "Green" to GreenColorScheme,
    "Orange" to OrangeColorScheme
)

// 计时器状态类，用于保持状态
class TimerState {
    var initialHours by mutableIntStateOf(0)
    var initialMinutes by mutableIntStateOf(0)
    var initialSeconds by mutableIntStateOf(0)
    var timerValue by mutableIntStateOf(0)
    var isRunning by mutableStateOf(false)
    var showDialog by mutableStateOf(false)
    var lastUpdateTime by mutableLongStateOf(0L)
    var coroutineScope: CoroutineScope? = null
}

// 秒表状态类，用于保持状态
class StopwatchState {
    var elapsedTime by mutableLongStateOf(0L)
    var isRunning by mutableStateOf(false)
    var startNano by mutableLongStateOf(0L)
    var accumulatedNano by mutableLongStateOf(0L)
    var laps by mutableStateOf(listOf<Pair<Int, Long>>())
    var coroutineScope: CoroutineScope? = null
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 使用remember保存主题选择
            var currentThemeIndex by remember { mutableIntStateOf(0) }
            val currentColorScheme = colorSchemes[currentThemeIndex].second

            // 创建计时器和秒表的状态对象
            val timerState = remember { TimerState() }
            val stopwatchState = remember { StopwatchState() }

            // 确保计时器在后台运行
            LaunchedEffect(timerState.isRunning) {
                if (timerState.isRunning && timerState.coroutineScope == null) {
                    timerState.coroutineScope = CoroutineScope(Dispatchers.Main)
                    timerState.coroutineScope?.launch {
                        while (timerState.isRunning && timerState.timerValue > 0) {
                            delay(1000)
                            timerState.timerValue--
                            timerState.lastUpdateTime = System.currentTimeMillis()
                        }
                        if (timerState.timerValue <= 0) {
                            timerState.isRunning = false
                        }
                    }
                } else if (!timerState.isRunning) {
                    timerState.coroutineScope?.cancel()
                    timerState.coroutineScope = null
                }
            }

            // 确保秒表在后台运行
            LaunchedEffect(stopwatchState.isRunning) {
                if (stopwatchState.isRunning && stopwatchState.coroutineScope == null) {
                    stopwatchState.coroutineScope = CoroutineScope(Dispatchers.Main)
                    stopwatchState.coroutineScope?.launch {
                        while (stopwatchState.isRunning) {
                            delay(50) // 每50ms更新一次以获得更流畅的显示
                            stopwatchState.elapsedTime = stopwatchState.accumulatedNano +
                                    (System.nanoTime() - stopwatchState.startNano)
                        }
                    }
                } else if (!stopwatchState.isRunning) {
                    stopwatchState.coroutineScope?.cancel()
                    stopwatchState.coroutineScope = null
                }
            }

            MaterialTheme(
                colorScheme = currentColorScheme,
                typography = MaterialTheme.typography
            ) {
                TimeApp(
                    currentThemeIndex = currentThemeIndex,
                    onThemeChange = { newIndex -> currentThemeIndex = newIndex },
                    timerState = timerState,
                    stopwatchState = stopwatchState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeApp(
    currentThemeIndex: Int,
    onThemeChange: (Int) -> Unit,
    timerState: TimerState,
    stopwatchState: StopwatchState
) {
    var currentTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("时钟", "计时器", "秒表")
    val currentColorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "timekeeper",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = currentColorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = currentColorScheme.primaryContainer,
                    titleContentColor = currentColorScheme.onPrimaryContainer
                ),
                actions = {
                    // 添加主题切换按钮
                    ThemeSwitcher(
                        currentThemeIndex = currentThemeIndex,
                        onThemeChange = onThemeChange
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = currentColorScheme.surfaceVariant
            ) {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.AccessTime, title)
                                1 -> Icon(Icons.Default.HourglassTop, title)
                                2 -> Icon(Icons.Default.Timer, title)
                                else -> Icon(Icons.Default.AccessTime, title)
                            }
                        },
                        label = { Text(title) },
                        selected = currentTab == index,
                        onClick = { currentTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = currentColorScheme.primaryContainer,
                            selectedIconColor = currentColorScheme.onPrimaryContainer,
                            selectedTextColor = currentColorScheme.onPrimaryContainer
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
                .background(currentColorScheme.surface)
        ) {
            // 使用交叉淡入淡出动画切换屏幕
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    // 添加滑动和淡入淡出组合动画
                    val direction = if (targetState > initialState) 1 else -1
                    (slideInHorizontally(
                        animationSpec = tween(durationMillis = 300),
                        initialOffsetX = { fullWidth -> direction * fullWidth }
                    ) + fadeIn(animationSpec = tween(300))).togetherWith(
                        slideOutHorizontally(
                            animationSpec = tween(durationMillis = 300),
                            targetOffsetX = { fullWidth -> -direction * fullWidth }
                        ) + fadeOut(animationSpec = tween(300)))
                }
            ) { tab ->
                when (tab) {
                    0 -> ClockScreen()
                    1 -> TimerScreen(timerState = timerState)
                    2 -> StopwatchScreen(stopwatchState = stopwatchState)
                    else -> ClockScreen()
                }
            }
        }
    }
}

// 主题切换器组件
@Composable
fun ThemeSwitcher(
    currentThemeIndex: Int,
    onThemeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.Palette,
                "切换主题",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            colorSchemes.forEachIndexed { index, (name, _) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            name,
                            color = if (index == currentThemeIndex) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                    onClick = {
                        onThemeChange(index)
                        expanded = false
                    },
                    leadingIcon = {
                        if (index == currentThemeIndex) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "已选择",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ClockScreen() {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    val currentDate = LocalDate.now()
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm:ss") }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy年MM月dd日") }
    val currentColorScheme = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = LocalTime.now()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 统一圆形元素尺寸 - 使用300.dp
        CircularElementContainer {
            // 时钟表盘背景
            Surface(
                modifier = Modifier.size(300.dp),
                shape = CircleShape,
                color = currentColorScheme.surfaceVariant,
                shadowElevation = 8.dp
            ) {}

            // 时间文本
            Text(
                text = currentTime.format(timeFormatter),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = currentColorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 日期信息
        Text(
            text = currentDate.format(dateFormatter),
            style = MaterialTheme.typography.titleLarge,
            color = currentColorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "当前时间",
            style = MaterialTheme.typography.titleMedium,
            color = currentColorScheme.onSurfaceVariant
        )
    }
}

// 统一的圆形元素容器，确保时钟和计时器中的圆形元素大小和位置一致
@Composable
fun CircularElementContainer(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(350.dp) // 稍大于圆形元素以提供空间
            .padding(16.dp),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun TimerScreen(timerState: TimerState) {
    val currentColorScheme = MaterialTheme.colorScheme

    // 计算初始时间（秒）
    val initialTime = remember(timerState.initialHours, timerState.initialMinutes, timerState.initialSeconds) {
        timerState.initialHours * 3600 + timerState.initialMinutes * 60 + timerState.initialSeconds
    }

    val progress = if (initialTime > 0) timerState.timerValue.toFloat() / initialTime else 1f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 顶部设置按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { timerState.showDialog = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "设置时间",
                    tint = currentColorScheme.primary
                )
            }
        }

        // 使用统一的圆形容器
        CircularElementContainer {
            // 环形进度条 - 使用与时钟相同的大小
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(300.dp),
                color = currentColorScheme.primary,
                strokeWidth = 12.dp,
                trackColor = currentColorScheme.surfaceVariant,
            )

            // 时间显示
            Text(
                text = String.format(
                    Locale.US,
                    "%02d:%02d:%02d",
                    timerState.timerValue / 3600,
                    (timerState.timerValue % 3600) / 60,
                    timerState.timerValue % 60
                ),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = currentColorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 控制按钮
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FilledTonalButton(
                onClick = {
                    timerState.timerValue = initialTime
                    timerState.isRunning = false
                },
                shape = CircleShape,
                enabled = timerState.timerValue != initialTime || timerState.isRunning
            ) {
                Icon(Icons.Default.Replay, "重置")
            }

            Button(
                onClick = {
                    timerState.isRunning = !timerState.isRunning
                    // 当重新开始时，重置最后更新时间
                    if (timerState.isRunning) timerState.lastUpdateTime = System.currentTimeMillis()
                },
                shape = CircleShape,
                enabled = timerState.timerValue > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = currentColorScheme.primary,
                    contentColor = currentColorScheme.onPrimary
                )
            ) {
                Icon(
                    if (timerState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    if (timerState.isRunning) "暂停" else "开始"
                )
            }
        }
    }

    // 时间设置对话框
    if (timerState.showDialog) {
        AlertDialog(
            onDismissRequest = { timerState.showDialog = false },
            title = { Text("设置计时器") },
            text = {
                Column {
                    Text("设置时间", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 小时输入
                        OutlinedTextField(
                            value = timerState.initialHours.takeIf { it > 0 }?.toString() ?: "",
                            onValueChange = { input ->
                                if (input.isEmpty()) {
                                    timerState.initialHours = 0
                                } else {
                                    input.filter { it.isDigit() }.toIntOrNull()?.takeIf { num ->
                                        num in 0..99
                                    }?.let {
                                        timerState.initialHours = it
                                    }
                                }
                            },
                            label = { Text("小时") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        // 分钟输入
                        OutlinedTextField(
                            value = timerState.initialMinutes.takeIf { it > 0 }?.toString() ?: "",
                            onValueChange = { input ->
                                if (input.isEmpty()) {
                                    timerState.initialMinutes = 0
                                } else {
                                    input.filter { it.isDigit() }.toIntOrNull()?.takeIf { num ->
                                        num in 0..59
                                    }?.let {
                                        timerState.initialMinutes = it
                                    }
                                }
                            },
                            label = { Text("分钟") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        // 秒输入
                        OutlinedTextField(
                            value = timerState.initialSeconds.takeIf { it > 0 }?.toString() ?: "",
                            onValueChange = { input ->
                                if (input.isEmpty()) {
                                    timerState.initialSeconds = 0
                                } else {
                                    input.filter { it.isDigit() }.toIntOrNull()?.takeIf { num ->
                                        num in 0..59
                                    }?.let {
                                        timerState.initialSeconds = it
                                    }
                                }
                            },
                            label = { Text("秒") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        timerState.timerValue = timerState.initialHours * 3600 +
                                timerState.initialMinutes * 60 +
                                timerState.initialSeconds
                        timerState.isRunning = false
                        timerState.showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentColorScheme.primary,
                        contentColor = currentColorScheme.onPrimary
                    )
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { timerState.showDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun StopwatchScreen(stopwatchState: StopwatchState) {
    val currentColorScheme = MaterialTheme.colorScheme

    fun formatTime(nanos: Long): String {
        val totalMillis = nanos / 1_000_000
        val millis = totalMillis % 1000
        val seconds = (totalMillis / 1000) % 60
        val minutes = (totalMillis / 60_000) % 60
        val hours = totalMillis / 3_600_000

        return if (hours > 0) {
            String.format(
                Locale.US,
                "%02d:%02d:%02d.%03d",
                hours, minutes, seconds, millis
            )
        } else {
            String.format(
                Locale.US,
                "%02d:%02d.%03d",
                minutes, seconds, millis
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 时间显示
        Text(
            text = formatTime(stopwatchState.elapsedTime),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp),
            color = currentColorScheme.onSurface
        )

        // 控制按钮
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            FilledTonalButton(
                onClick = {
                    stopwatchState.laps = stopwatchState.laps +
                            (stopwatchState.laps.size + 1 to stopwatchState.elapsedTime)
                },
                enabled = stopwatchState.isRunning,
                shape = CircleShape
            ) {
                Text("计次")
            }

            Button(
                onClick = {
                    stopwatchState.isRunning = !stopwatchState.isRunning
                    if (stopwatchState.isRunning) {
                        // 如果是重新开始，记录开始时间
                        stopwatchState.startNano = System.nanoTime()
                    } else {
                        // 如果暂停，保存已累计的时间
                        stopwatchState.accumulatedNano = stopwatchState.elapsedTime
                    }
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = currentColorScheme.primary,
                    contentColor = currentColorScheme.onPrimary
                )
            ) {
                Icon(
                    if (stopwatchState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    if (stopwatchState.isRunning) "暂停" else "开始"
                )
            }

            FilledTonalButton(
                onClick = {
                    stopwatchState.isRunning = false
                    stopwatchState.elapsedTime = 0
                    stopwatchState.accumulatedNano = 0
                    stopwatchState.laps = emptyList()
                },
                enabled = stopwatchState.elapsedTime > 0 || stopwatchState.laps.isNotEmpty(),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Replay, "重置")
            }
        }

        // 计次列表
        if (stopwatchState.laps.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = MaterialTheme.shapes.medium,
                color = currentColorScheme.surfaceVariant
            ) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(stopwatchState.laps.reversed()) { (lapNumber, lapTime) ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    "计次 $lapNumber",
                                    fontWeight = FontWeight.Medium,
                                    color = currentColorScheme.onSurface
                                )
                            },
                            trailingContent = {
                                Text(
                                    formatTime(lapTime),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = currentColorScheme.onSurface
                                )
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        if (lapNumber < stopwatchState.laps.size) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = currentColorScheme.onSurface.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
            }
        }
    }
}