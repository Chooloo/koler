package com.chooloo.www.callmanager.util;

import java.util.Locale;

/**
 * Just a simple stopwatch class
 */
public class Stopwatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean isRunning = false;

    /**
     * Starts the timer
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
    }

    /**
     * Stops the timer
     */
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.isRunning = false;
    }

    /**
     * Elapsed time in milliseconds
     */
    public long getElapsedTime() {
        if (isRunning) return System.currentTimeMillis() - startTime;
        return stopTime - startTime;
    }

    /**
     * Elapsed time in seconds
     */
    public long getElapsedTimeSecs() {
        if (isRunning) return ((System.currentTimeMillis() - startTime) / 1000);
        return ((stopTime - startTime) / 1000);
    }

    /**
     * Returns the time in a string format
     */
    public String getStringTime() {
        long currentTime = getElapsedTime();
        int seconds = (int) currentTime / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds -= minutes * 60;
        minutes -= hours * 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}