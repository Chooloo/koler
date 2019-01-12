package com.chooloo.www.callmanager;

public class Stopwatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    // Starts the timer
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    // Stops the timer
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    // Elaspsed time in milliseconds
    public long getElapsedTime() {
        if (running) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }

    // Elaspsed time in seconds
    public long getElapsedTimeSecs() {
        if (running) {
            return ((System.currentTimeMillis() - startTime) / 1000);
        }
        return ((stopTime - startTime) / 1000);
    }

    // Returns the time in a string format (ToString)
    public String getStringTime() {
        long currentTime = getElapsedTime();
        int seconds = (int) currentTime / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds -= minutes * 60;
        minutes -= hours * 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // sample usage
    public static void main(String[] args) {
        Stopwatch s = new Stopwatch();
        s.start();
        // code you want to time goes here
        s.stop();
        System.out.println("elapsed time in milliseconds: " + s.getElapsedTime());
    }

}