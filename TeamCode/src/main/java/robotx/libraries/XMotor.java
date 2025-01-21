package robotx.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * XMotor Class
 * Use this class to better incorporate encoders into motor usage.
 * <p>
 * Created by John Daniher 01/09/25
 */

public class XMotor {
    private final OpMode op;
    private final String motorPath;

    //Constructor; no range given
    public XMotor(OpMode op, String motorPath) {
        this.op = op;
        this.motorPath = motorPath;
        min = Integer.MIN_VALUE;
        max = Integer.MAX_VALUE;
    }

    //Constructor; minimum and maximum values given
    public XMotor(OpMode op, String motorPath, int max, int min) {
        this.op = op;
        this.motorPath = motorPath;
        this.max = max;
        this.min = min;
    }

    private DcMotor motor;
    private double power = 0;

    private boolean fixed = false;
    private int targetPosition = 0;
    public int position = 0;

    private final StopWatch stopWatch = new StopWatch();

    private int min;
    private int max;

    //Initializes motor; maps motor, sets mode to indefinite, resets encoder values
    public void init() {
        motor = op.hardwareMap.dcMotor.get(motorPath);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        reset();
    }

    //Sets the power of the motor; DOES NOT CHANGE MODE
    public void setPower(double power) {
        this.power = power;
        motor.setPower(power);
    }

    //Stops the motor; sets power to 0, stops timer, and changes mode to indefinite
    public void stop() {
        power = 0;
        setIndefiniteRotation(0);
    }

    //Changes the mode of the motor to indefinite; does not set power
    public void setIndefiniteRotation() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        cancelTimer();
        fixed = false;
    }

    //Changes the mode of the motor to indefinite and sets the power
    public void setIndefiniteRotation(double power) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        cancelTimer();
        fixed = false;

        setPower(power);
    }

    //Changes the mode of the motor to indefinite but schedules a stop
    public void setTimedRotation(int milliseconds) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        stopWatch.startTimer(milliseconds);
        fixed = false;
    }

    //Changes the mode of the motor to indefinite and sets the power, but schedules a stop
    public void setTimedRotation(int milliseconds, double power) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        stopWatch.startTimer(milliseconds);
        fixed = false;

        setPower(power);
    }

    //Changes the mode of the motor to position based and sets the position
    public void setFixedRotation(int targetPosition) {
        this.targetPosition = targetPosition;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    //Changes the mode of the motor to position based and sets the position and power
    public void setFixedRotation(int targetPosition, double power) {
        this.targetPosition = targetPosition;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
    }

    //Cancels the timer scheduled to stop the motor
    public void cancelTimer() {
        stopWatch.clearTimer();
    }

    //Resets the encoder values
    public void reset() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //Refreshes the position variable of the motor
    public void refreshPosition() {
        position = motor.getCurrentPosition();
    }

    public void increment(int incrementAmount) {
        refreshPosition();
        setFixedRotation(position + incrementAmount);
    }

    //Sets the minimum position of the encoder
    public void setMin(int min) {
        this.min = min;
    }

    //Sets the maximum position of the encoder
    public void setMax(int max) {
        this.max = max;
    }

    //Loop method
    public void loop() {
        refreshPosition();

        //Ensures encoder within range
        if (position < min) {
            setFixedRotation(min);
        } else if (position > max) {
            setFixedRotation(max);
        }

        //If timer has elapsed, stop motor
        if (stopWatch.timerDone()) {
            stop();
        }
        //If motor isn't moving
        if (fixed && !motor.isBusy()) {
            stop();
        }
    }
}