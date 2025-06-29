package com.Ayin.clock.ui.stopwatch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchViewModel : ViewModel() {
    var elapsedTime by mutableLongStateOf(0L)
    var isRunning by mutableStateOf(false)
    var startNano by mutableLongStateOf(0L)
    var accumulatedNano by mutableLongStateOf(0L)
    var laps by mutableStateOf(listOf<Pair<Int, Long>>())

    private var job: Job? = null
    private var lastUpdateTime = 0L

    fun startStopwatch() {
        if (isRunning) return

        isRunning = true
        startNano = System.nanoTime()
        job?.cancel()
        job = viewModelScope.launch {
            while (isRunning) {
                val currentTime = System.nanoTime()
                // 限制更新频率为每秒15次
                if (currentTime - lastUpdateTime > 66_666_666) {
                    elapsedTime = accumulatedNano + (currentTime - startNano)
                    lastUpdateTime = currentTime
                }
                delay(16) // ~60fps
            }
        }
    }

    fun pauseStopwatch() {
        isRunning = false
        accumulatedNano = elapsedTime
    }

    fun resetStopwatch() {
        isRunning = false
        elapsedTime = 0
        accumulatedNano = 0
        laps = emptyList()
    }

    fun addLap() {
        laps = laps + (laps.size + 1 to elapsedTime)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}