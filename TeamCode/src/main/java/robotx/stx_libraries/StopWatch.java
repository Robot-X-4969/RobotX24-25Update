package robotx.stx_libraries;

import java.util.concurrent.TimeUnit;

/**
 * StopWatch Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement timer handling.
 * <p>
 * Created by Evan on 10/29/2015.
 */
public class StopWatch {
    private long tStart = 0;
    private long target = 0;
    /**
     * The amount of resets the timer has gone through.
     */
    public int resetCounter = 0;

    /**
     * Object to manage time-based measurements.
     */
    public StopWatch() {
    }

    /**
     * Object to manage time-based measurements with a preset timer.
     *
     * @param millis Timer length in milliseconds.
     */
    public StopWatch(long millis) {
        startTimer(millis);
    }

    /**
     * Resets the StopWatch's timer.
     */
    public void reset() {
        tStart = System.nanoTime();
        resetCounter++;
    }

    /**
     * Gets the time since last timer reset.
     * @return Elapsed time in nanoseconds.
     */
    public long elapsedNanos() {
        return (System.nanoTime() - tStart);
    }

    /**
     * Gets the time since last timer reset.
     * @return Elapsed time in milliseconds.
     */
    public long elapsedMillis() {
        return TimeUnit.NANOSECONDS.toMillis(elapsedNanos());
    }

    /**
     * Starts a new timer.
     * @param millis Timer length in milliseconds.
     */
    public void startTimer(long millis) {
        reset();
        target = millis;
    }

    /**
     * Clears any existing timer.
     */
    public void clearTimer() {
        target = 0;
    }

    /**
     * Checks if set timer has finished.
     * @return true if done; false if ongoing
     */
    public boolean timerDone() {
        return target != 0 && elapsedMillis() >= target;
    }
}
