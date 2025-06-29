package com.Ayin.clock.ui.clock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Ayin.clock.ui.components.CircularElementContainer
import com.Ayin.clock.util.TimeFormatter
import kotlinx.coroutines.delay

@Composable
fun ClockScreen() {
    var currentTime by remember { mutableStateOf(TimeFormatter.currentTime()) }
    val currentColorScheme = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = TimeFormatter.currentTime()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularElementContainer() {
            Surface(
                modifier = Modifier.size(300.dp),
                shape = CircleShape,
                color = currentColorScheme.surfaceVariant,
                shadowElevation = 8.dp
            ) {}

            Text(
                text = currentTime,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = currentColorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = TimeFormatter.currentDate(),
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