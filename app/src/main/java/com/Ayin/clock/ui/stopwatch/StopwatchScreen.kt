package com.Ayin.clock.ui.stopwatch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Ayin.clock.ui.components.ControlButton
import com.Ayin.clock.util.TimeFormatter

@Composable
fun StopwatchScreen(
    viewModel: StopwatchViewModel = viewModel()
) {
    val currentColorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = TimeFormatter.formatStopwatch(viewModel.elapsedTime),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(vertical = 32.dp),
            color = currentColorScheme.onSurface
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            ControlButton(
                icon = Icons.Default.Flag,
                contentDescription = "计次",
                onClick = { viewModel.addLap() },
                enabled = viewModel.isRunning
            )

            ControlButton(
                icon = if (viewModel.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (viewModel.isRunning) "暂停" else "开始",
                onClick = {
                    if (viewModel.isRunning) viewModel.pauseStopwatch() else viewModel.startStopwatch()
                },
                isPrimary = true
            )

            ControlButton(
                icon = Icons.Default.Replay,
                contentDescription = "重置",
                onClick = { viewModel.resetStopwatch() },
                enabled = viewModel.elapsedTime > 0 || viewModel.laps.isNotEmpty()
            )
        }

        if (viewModel.laps.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = MaterialTheme.shapes.medium,
                color = currentColorScheme.surfaceVariant
            ) {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(viewModel.laps.reversed()) { (lapNumber, lapTime) ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    "计次 $lapNumber",
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                                    color = currentColorScheme.onSurface
                                )
                            },
                            trailingContent = {
                                Text(
                                    TimeFormatter.formatStopwatch(lapTime),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = currentColorScheme.onSurface
                                )
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        if (lapNumber < viewModel.laps.size) {
                            Divider(
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