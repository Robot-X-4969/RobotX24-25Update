package robotx.stx_libraries;

import java.util.ArrayList;
import java.util.List;

/**
 * Scheduler Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement scheduling events in mass quantities.
 * <p>
 * Created by John Daniher on 1/28/2025
 */
public class Scheduler {
    private final List<Event> events = new ArrayList<>();
    private final List<String> ids = new ArrayList<>();

    /**
     * Scheduling object which allows you to schedule methods to run in the future without the use of threads.
     */
    public Scheduler() {
    }

    /**
     * Schedules a executable to run in a given time.
     *
     * @param millis   The time in milliseconds until the method runs.
     * @param runnable The executable to run.
     */
    public Event schedule(long millis, Runnable runnable) {
        String id = Event.randomID(5);
        while (ids.contains(id)) {
            id = Event.randomID(5);
        }
        final Event event = new Event(millis, id, runnable);
        events.add(event);
        ids.add(id);
        return event;
    }

    /**
     * Schedules a executable with a given id to run in a given time.
     *
     * @param millis   The time in milliseconds until the method runs.
     * @param id       The String id of the executable.
     * @param runnable The executable to run.
     */
    public Event schedule(long millis, String id, Runnable runnable) {
        if (!ids.contains(id)) {
            final Event event = new Event(millis, id, runnable);
            events.add(event);
            ids.add(id);
            return event;
        }
        return null;
    }

    /**
     * Cancels the future execution of an event with a given id.
     *
     * @param id The String id of the executable to cancel.
     */
    public void cancel(String id) {
        if (ids.contains(id)) {
            events.remove(events.get(ids.indexOf(id)));
            ids.remove(id);
        }
    }

    /**
     * Loop method which checks which executables to run.
     */
    public void loop() {
        for (Event event : events) {
            if (event.stopWatch.timerDone()) {
                event.run();
                events.remove(event);
            }
        }
    }
}
