package com.Ayin.clock.ui.timer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Ayin.clock.ui.components.CircularElementContainer
import com.Ayin.clock.ui.components.ControlButton
import com.Ayin.clock.util.TimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel = viewModel()
) {
    val currentColorScheme = MaterialTheme.colorScheme

    // 實時計算初始時間（移除remember）
    val initialTime = viewModel.initialHours * 3600 +
            viewModel.initialMinutes * 60 +
            viewModel.initialSeconds

    // 處理除零問題
    val progress = if (initialTime > 0) {
        (viewModel.timerValue.toFloat() / initialTime).coerceIn(0f, 1f)
    } else {
        1f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { viewModel.showDialog = true },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "设置时间",
                    tint = currentColorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        CircularElementContainer {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(300.dp),
                color = currentColorScheme.primary,
                strokeWidth = 12.dp,
                trackColor = currentColorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round // 添加圓角端點
            )

            Text(
                text = TimeFormatter.formatTimer(viewModel.timerValue),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = currentColorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            ControlButton(
                icon = Icons.Default.Replay,
                contentDescription = "重置",
                onClick = { viewModel.resetTimer() },
                enabled = viewModel.timerValue != initialTime || viewModel.isRunning
            )

            ControlButton(
                icon = if (viewModel.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (viewModel.isRunning) "暂停" else "开始",
                onClick = {
                    if (viewModel.isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                },
                enabled = viewModel.timerValue > 0,
                isPrimary = true
            )
        }
    }

    if (viewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDialog = false },
            title = { Text("设置计时器") },
            text = {
                Column {
                    Text("设置时间", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = viewModel.initialHours.takeIf { it > 0 }?.toString() ?: "",
                            onValueChange = { input ->
                                viewModel.initialHours = input.filter { it.isDigit() }.toIntOrNull()?.coerceIn(0..99) ?: 0
                            },
                            label = { Text("小时") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = viewModel.initialMinutes.takeIf { it > 0 }?.toString() ?: "",
                            onValueChange = { input ->
                                viewModel.initialMinutes = input.filter { it.isDigit() }.toIntOrNull()?.coerceIn(0..59) ?: 0
                            },
                            label = { Text("分钟") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = viewModel.initialSeconds.takeIf { it > 0 }?.toString() ?: "",
                            onValueChange = { input ->
                                viewModel.initialSeconds = input.filter { it.isDigit() }.toIntOrNull()?.coerceIn(0..59) ?: 0
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
                        viewModel.setInitialTime()
                        viewModel.showDialog = false
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
                TextButton(onClick = { viewModel.showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}