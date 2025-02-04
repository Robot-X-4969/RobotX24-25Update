package robotx.stx_libraries;

import java.util.Random;

/**
 * Event Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement runnable events.
 * <p>
 * Created by John Daniher on 1/29/2025
 */
public class Event {
    /**
     * The String id of the event.
     */
    public final String id;
    /**
     * The executable of the event.
     */
    public final Runnable event;
    /**
     * The timer component of the event.
     */
    public final Stopwatch stopwatch;

    /**
     * Future Event object which contains an executable and timer.
     *
     * @param millis The duration in milliseconds of the timer.
     * @param event The executable of the event.
     */
    public Event(long millis, Runnable event){
        id = randomID(5);
        this.event = event;
        stopwatch = new Stopwatch(millis);
    }

    /**
     * Future Event object which contains an executable, timer, and given id.
     *
     * @param millis The duration in milliseconds of the timer.
     * @param id The String id of the event.
     * @param event The executable of the event.
     */
    public Event(long millis, String id, Runnable event){
        this.id = id;
        this.event = event;
        stopwatch = new Stopwatch(millis);
    }

    /**
     * Generates a random String id of given length.
     *
     * @param length Length of the String id.
     * @return A randomized string of set length.
     */
    public static String randomID(int length){
        final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < length; i++){
            final int randomInt = random.nextInt(62);
            builder.append(chars.charAt(randomInt));
        }

        return builder.toString();
    }

    /**
     * Runs the executable of the event.
     */
    public void run() {
        event.run();
    }
}
