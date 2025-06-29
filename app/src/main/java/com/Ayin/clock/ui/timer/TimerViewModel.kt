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

    fun startTimer() {
        if (isRunning || timerValue <= 0) return

        isRunning = true
        job?.cancel()
        job = viewModelScope.launch {
            while (isRunning && timerValue > 0) {
                delay(1000)
                timerValue--
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