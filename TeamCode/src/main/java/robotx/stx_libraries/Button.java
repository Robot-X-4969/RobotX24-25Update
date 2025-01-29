package robotx.stx_libraries;

/**
 * Button Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement gamepad button handling.
 * <p>
 * Created by Nicholas on 11/21/16.
 */
public class Button {
    // Recorded button states
    private boolean lastValue = false;
    private boolean currentValue = false;

    /**
     * Updates the state of the button based on a privated boolean.
     *
     * @param newValue The current value of the button.
     */
    public void update(boolean newValue) {
        lastValue = currentValue;
        currentValue = newValue;
    }

    /**
     * Whether or not the button is currently down.
     * @return true if down; false if up
     */
    public boolean isDown() {
        return currentValue;
    }

    /**
     * Whether or not the button is currently up.
     * @return true if up; false if down.
     */
    public boolean isUp() {
        return !currentValue;
    }

    /**
     * Whether or not the button was pressed only in the most recent check.
     * @return true if only now down; else false
     */
    public boolean wasPressed() {
        return !lastValue && currentValue;
    }

    /**
     * Whether or not the button was released only in the most recent check.
     * @return true if only now up; else false
     */
    public boolean wasReleased() {
        return lastValue && !currentValue;
    }
}
