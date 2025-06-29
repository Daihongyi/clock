package com.Ayin.clock.ui.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    var initialHours by mutableIntStateOf(0)
    var initialMinutes by mutableIntStateOf(0)
    var initialSeconds by mutableIntStateOf(0)
    var timerValue by mutableIntStateOf(0)
    var isRunning by mutableStateOf(false)
    var showDialog by mutableStateOf(false)

    private var job: Job? = null
    private var lastUpdateTime = 0L // 記錄上次更新時間

    fun startTimer() {
        if (isRunning) return

        isRunning = true
        lastUpdateTime = System.currentTimeMillis() // 記錄開始時間

        job?.cancel()
        job = viewModelScope.launch {
            while (isRunning && timerValue > 0) {
                delay(16) // 約60fps更新
                val currentTime = System.currentTimeMillis()
                val elapsed = (currentTime - lastUpdateTime).coerceAtLeast(0)

                if (elapsed >= 1000) {
                    timerValue -= (elapsed / 1000).toInt()
                    lastUpdateTime = currentTime - (elapsed % 1000)
                }
            }
            isRunning = false
        }
    }

    fun pauseTimer() {
        isRunning = false
        job?.cancel()
    }

    fun resetTimer() {
        isRunning = false
        job?.cancel()
        timerValue = initialHours * 3600 + initialMinutes * 60 + initialSeconds
    }

    fun setInitialTime() {
        timerValue = initialHours * 3600 + initialMinutes * 60 + initialSeconds
        isRunning = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}