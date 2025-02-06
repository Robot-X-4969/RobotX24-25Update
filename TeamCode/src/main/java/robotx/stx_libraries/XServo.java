package robotx.stx_libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ServoImplEx;

/**
 * XServo Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement servos.
 * <p>
 * Created by John Daniher 1/02/25
 */
public class XServo {
    private ServoImplEx servo;
    private final String servoPath;
    private final OpMode op;

    public boolean enabled = true;

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
    public int index = 0;
    private int lastIndex = 0;

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
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    /**
     * Initiates the servo by mapping it and sets the default position.
     */
    public void init() {
        servo = op.hardwareMap.get(ServoImplEx.class, servoPath);
        servo.setPosition(positions[index]);
    }

    /**
     * Sets the servo position to a given index of the provided positions array.
     *
     * @param index The index of the requested position in the provided positions array.
     */
    public void setIndex(int index) {
        lastIndex = index;
        this.index = index;
        servo.setPosition(positions[index]);
    }

    /**
     * Cycles forward through the provided positions array.
     */
    public void forward() {
        index++;
        if (index >= positions.length) {
            index = 0;
        }
        setIndex(index);
    }

    /**
     * Cycles backward through the provided positions array.
     */
    public void backward() {
        index--;
        if (index < 0) {
            index = positions.length - 1;
        }
        setIndex(index);
    }

    /**
     * Returns to the last set position.
     */
    public void backtrack() {
        setIndex(lastIndex);
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
     * @param position The position to move the servo to.
     */
    public void setPosition(double position) {
        this.position = Math.max(min, Math.max(min, position));
        servo.setPosition(this.position);
    }

    public void disable(){
        enabled = false;
        servo.setPwmDisable();
    }

    public void enable(){
        enabled = true;
        servo.setPwmEnable();
    }

    public void toggleEnabled(){
        enabled = !enabled;
        if(enabled){
            enable();
        } else {
            disable();
        }
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        if(enabled){
            enable();
        } else {
            disable();
        }
    }

    /**
     * Sets the minimum position of the servo.
     *
     * @param min The new minimum position of the servo.
     */
    public void setMin(double min) {
        this.min = Math.min(min, max);
        setPosition(position);
    }

    /**
     * Sets the maximum position of the servo.
     *
     * @param max The new maximum position of the servo.
     */
    public void setMax(double max) {
        this.max = Math.max(max, min);
        setPosition(position);
    }

    /**
     * Sets the servo's minimum and maximum positions.
     *
     * @param min The new minimum position of the servo.
     * @param max The new maximum position of the servo.
     */
    public void setRange(double min, double max) {
        this.min = Math.min(min, max);
        this.max = Math.max(max, min);
        setPosition(position);
    }

    /**
     * Provides a new array of positions for the servo to cycle through.
     *
     * @param positions The new array of positions.
     */
    public void setPositions(double[] positions) {
        this.positions = positions;
        if (index >= positions.length) {
            this.index = positions.length - 1;
        }
        if (lastIndex >= positions.length) {
            lastIndex = positions.length - 1;
        }
    }
}