package com.Ayin.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Ayin.clock.ui.main.TimeApp
import com.Ayin.clock.ui.stopwatch.StopwatchViewModel
import com.Ayin.clock.ui.theme.ThemeColors
import com.Ayin.clock.ui.timer.TimerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val timerViewModel: TimerViewModel = viewModel()
            val stopwatchViewModel: StopwatchViewModel = viewModel()

            // 设置默认主题
            val defaultTheme = ThemeColors.schemes[0].second
            MaterialTheme(
                colorScheme = androidx.compose.material3.lightColorScheme(
                    primary = defaultTheme.primary,
                    onPrimary = Color.White,
                    secondary = defaultTheme.secondary,
                    onSecondary = Color.Black,
                    tertiary = defaultTheme.tertiary,
                    onTertiary = Color.White,
                    surface = defaultTheme.surface,
                    onSurface = defaultTheme.onSurface,
                    background = defaultTheme.surface,
                    onBackground = defaultTheme.onSurface
                )
            ) {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    TimeApp(
                        timerViewModel = timerViewModel,
                        stopwatchViewModel = stopwatchViewModel
                    )
                }
            }
        }
    }
}