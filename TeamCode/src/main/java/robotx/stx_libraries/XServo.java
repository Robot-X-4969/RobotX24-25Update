package robotx.stx_libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * XServo Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement servos.
 * <p>
 * Created by John Daniher 1/02/25
 */
public class XServo {
    private Servo servo;
    private final String servoPath;
    private final OpMode op;

    /**
     * The minimum position of the servo's set range.
     */
    public double min;
    /**
     * The maximum position of the servo's set range.
     */
    public double max;

    /**
     * The servo's current set position.
     */
    public double position;

    private double[] positions;

    /**
     * The current index of the servo's position in the provided positions array.
     */
    public int state = 0;
    private int lastState = 0;

    /**
     * XServo Constructor
     * <p>
     * Implements servo set to cycle through a given array of positions more efficiently.
     *
     * @param op        The OpMode in which this servo runs.
     * @param servoPath The name the servo is configured to through the RevHub.
     * @param positions An array of servo positions to cycle through.
     */
    public XServo(OpMode op, String servoPath, double[] positions) {
        this.op = op;
        this.servoPath = servoPath;
        this.positions = positions;
        min = 0;
        max = 1;
    }

    /**
     * XServo Constructor
     * <p>
     * Implements servo set to focus on single position more efficiently.
     *
     * @param op        The OpMode in which this servo runs.
     * @param servoPath The name the servo is configured to through the RevHub.
     * @param position  The servo's preset position.
     */
    public XServo(OpMode op, String servoPath, double position) {
        this.op = op;
        this.servoPath = servoPath;
        this.positions = new double[]{position};
        min = 0;
        max = 1;
    }

    /**
     * XServo Constructor
     * <p>
     * Implements servo set to increment within a set range more efficiently.
     *
     * @param op        The OpMode in which this servo runs.
     * @param servoPath The name the servo is configured to through the RevHub.
     * @param position  The servo's preset position.
     * @param min       The servo's minimum position.
     * @param max       The servo's maximum position.
     */
    public XServo(OpMode op, String servoPath, double position, double min, double max) {
        this.op = op;
        this.servoPath = servoPath;
        this.positions = new double[]{position};
        this.min = min;
        this.max = max;
    }

    /**
     * Initiates the servo by mapping it and sets the default position.
     */
    public void init() {
        servo = op.hardwareMap.servo.get(servoPath);
        servo.setPosition(positions[state]);
    }

    /**
     * Sets the servo position to a given index of the provided positions array.
     *
     * @param index The index of the requested position in the provided positions array.
     */
    public void setIndex(int index) {
        lastState = state;
        state = index;
        servo.setPosition(positions[state]);
    }

    /**
     * Cycles forward through the provided positions array.
     */
    public void forward() {
        state++;
        if (state >= positions.length) {
            state = 0;
        }
        setIndex(state);
    }

    /**
     * Cycles backward through the provided positions array.
     */
    public void backward() {
        state--;
        if (state < 0) {
            state = positions.length - 1;
        }
        setIndex(state);
    }

    /**
     * Returns to the last set position.
     */
    public void backtrack() {
        setIndex(lastState);
    }

    /**
     * Increments the servo's current position by a provided decimal.
     *
     * @param increment The amount to increment the servo's position by.
     */
    public void increment(double increment) {
        servo.setPosition(servo.getPosition() + increment);
    }

    /**
     * Sets the servo's position to a given position.
     *
     * @param newPosition The position to move the servo to.
     */
    public void setPosition(double newPosition) {
        position = newPosition;
        if (position > max) {
            position = max;
        } else if (position < min) {
            position = min;
        }
        servo.setPosition(position);
    }

    /**
     * Sets the minimum position of the servo.
     *
     * @param min The new minimum position of the servo.
     */
    public void setMin(double min) {
        this.min = min;
        setPosition(position);
    }

    /**
     * Sets the maximum position of the servo.
     *
     * @param max The new maximum position of the servo.
     */
    public void setMax(double max) {
        this.max = max;
        setPosition(position);
    }

    /**
     * Provides a new array of positions for the servo to cycle through.
     *
     * @param positions The new array of positions.
     */
    public void setPositions(double[] positions) {
        this.positions = positions;
        if (state >= positions.length) {
            this.state = positions.length - 1;
        }
        if (lastState >= positions.length) {
            lastState = positions.length - 1;
        }
    }
}