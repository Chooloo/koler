package com.chooloo.www.koler.util

import android.os.Handler
import java.util.*

/**
 * Just a simple stopwatch class
 */
class Stopwatch {
    private var _startTime: Long = 0
    private var _stopTime: Long = 0
    private var _isRunning = false
    private var _handler: Handler? = null
    private var _onTimeStringChanged: (String) -> Unit = {}

    val elapsedTime: Long
        get() = if (_isRunning) System.currentTimeMillis() - _startTime else _stopTime - _startTime

    val elapsedTimeSecs: Long
        get() = if (_isRunning) (System.currentTimeMillis() - _startTime) / 1000 else (_stopTime - _startTime) / 1000

    fun start() {
        _startTime = System.currentTimeMillis()
        _isRunning = true
        _handler = Handler().apply {
            postDelayed({ _onTimeStringChanged.invoke(stringTime) }, 1000)
        }
    }

    fun stop() {
        _stopTime = System.currentTimeMillis()
        _isRunning = false
        _handler?.removeCallbacksAndMessages(null)
        _handler = null
    }

    val stringTime: String
        get() {
            val currentTime = elapsedTime
            var seconds = currentTime.toInt() / 1000
            var minutes = seconds / 60
            val hours = minutes / 60
            seconds -= minutes * 60
            minutes -= hours * 60
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        }

    fun registerListener(onTimeChangedListener: (String) -> Unit = {}) {
        _onTimeStringChanged = onTimeChangedListener
    }
}