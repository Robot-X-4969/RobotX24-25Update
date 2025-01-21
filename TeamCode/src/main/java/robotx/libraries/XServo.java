package robotx.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * XServo Class
 * Use this class to better implement servos; see public methods for more
 * <p>
 * Created by John Daniher 01/02/25
 */

public class XServo {
    //Constructor; Requires servo hardware map name and all positions
    public XServo(OpMode op, String servoPath, double[] positions) {
        this.op = op;
        this.servoPath = servoPath;
        this.positions = positions;
    }

    public XServo(OpMode op, String servoPath, double position) {
        this.op = op;
        this.servoPath = servoPath;
        this.positions = new double[]{position};
    }

    private Servo servo;
    private final String servoPath;
    private final double[] positions;
    private final OpMode op;

    private int state = 0;
    private int lastState = 0;

    //Initiates Servo
    public void init() {
        servo = op.hardwareMap.servo.get(servoPath);
        servo.setPosition(positions[state]);
    }

    //Sets servo position to specific index
    public void setIndex(int index) {
        lastState = state;
        state = index;
        servo.setPosition(positions[state]);
    }

    //Cycles forward through positions array
    public void forward() {
        state++;
        if (state >= positions.length) {
            state = 0;
        }
        setIndex(state);
    }

    //Cycles backward through positions array
    public void backward() {
        state--;
        if (state < 0) {
            state = positions.length - 1;
        }
        setIndex(state);
    }

    //Goes to last set servo position
    public void backtrack() {
        setIndex(lastState);
    }

    //Increments the servo position by inputted double
    public void increment(double increment) {
        servo.setPosition(servo.getPosition() + increment);
    }

    //Starts movement to new position
    public void setPosition(double position) {
        servo.setPosition(position);
    }
}