package robotx.libraries;

import java.util.concurrent.TimeUnit;

/**
 * Created by Evan on 10/29/2015.
 Use this class for timing.
 */
public class StopWatch {
    private long tStart = 0;
    private long target = 0;
    public int resetCounter = 0;

    //Constructor; no params
    public StopWatch() {}

    //Resets the timer
    public void reset() {
        tStart = System.nanoTime();
        resetCounter++;
    }

    //Gets the time in nanoseconds since last timer set
    public long elapsedNanos() {
        return (System.nanoTime() - tStart);
    }

    //Gets the time in milliseconds since last timer set
    public long elapsedMillis() {
        return TimeUnit.NANOSECONDS.toMillis( elapsedNanos() );
    }

    //Starts a timer of length millis (milliseconds)
    public void startTimer(long millis) {
        reset();
        target = millis;
    }

    //Clears any existing timer
    public void clearTimer(){
        target = 0;
    }

    //Checks if timer has passed given time; returns false if not set
    public boolean timerDone() {
        return target != 0 && elapsedMillis() >= target;
    }
}
