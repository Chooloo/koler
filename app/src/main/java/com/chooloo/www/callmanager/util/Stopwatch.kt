package com.chooloo.www.callmanager.util

import java.util.*

/**
 * Just a simple stopwatch class
 */
class Stopwatch {
    private var startTime: Long = 0
    private var stopTime: Long = 0
    private var isRunning = false

    /**
     * Elapsed time in milliseconds
     */
    val elapsedTime: Long
        get() = if (isRunning) System.currentTimeMillis() - startTime else stopTime - startTime

    /**
     * Elapsed time in seconds
     */
    val elapsedTimeSecs: Long
        get() = if (isRunning) (System.currentTimeMillis() - startTime) / 1000 else (stopTime - startTime) / 1000

    /**
     * Starts the timer
     */
    fun start() {
        startTime = System.currentTimeMillis()
        isRunning = true
    }

    /**
     * Stops the timer
     */
    fun stop() {
        stopTime = System.currentTimeMillis()
        isRunning = false
    }

    /**
     * Returns the time in a string format
     */
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
}