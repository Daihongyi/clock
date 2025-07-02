package com.ayin.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ayin.clock.ui.main.TimeApp
import com.ayin.clock.ui.stopwatch.StopwatchViewModel
import com.ayin.clock.ui.theme.ThemeColors
import com.ayin.clock.ui.timer.TimerViewModel
import kotlin.collections.get

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val timerViewModel: TimerViewModel = viewModel()
            val stopwatchViewModel: StopwatchViewModel = viewModel()

            // 设置默认主题
            val defaultTheme = ThemeColors.schemes[0].second
            MaterialTheme(
                colorScheme = lightColorScheme(
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
                    modifier = Modifier.fillMaxSize(),
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