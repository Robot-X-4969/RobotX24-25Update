package robotx.stx_libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * XMotor Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement encoder motors.
 * <p>
 * Created by John Daniher 1/09/25
 */
public class XMotor {
    private final OpMode op;
    private final String motorPath;

    private DcMotor motor;
    /**
     * The power the motor is currently set to.
     */
    public double power = 0;

    private boolean fixed = false;
    private int targetPosition = 0;
    public int position = 0;

    private final StopWatch stopWatch = new StopWatch();

    private boolean brakes = true;
    private boolean safe = true;
    private double safeRPM = 300;

    private long tpr = 1500;
    private double rpm = 0;

    private double rpmCoef = 0;
    private boolean rpmCoefSet = false;
    private StopWatch rpmStopWatch = new StopWatch(100);
    private int lastPos = 0;

    private int min;
    private int max;

    /**
     * XMotor Constructor
     * <p>
     * Implements DcMotor with encoders more efficiently.
     *
     * @param op        The OpMode in which the motor runs.
     * @param motorPath The name the motor is configured to through the RevHub.
     */
    public XMotor(OpMode op, String motorPath) {
        this.op = op;
        this.motorPath = motorPath;
        min = Integer.MIN_VALUE;
        max = Integer.MAX_VALUE;
    }

    /**
     * XMotor Constructor
     * <p>
     * Implements DcMotor with encoders monitoring rpm more efficiently.
     * <p>
     * See manufacturer data for values.
     *
     * @param op        The OpMode in which the motor runs.
     * @param motorPath The name the motor is configured to through the RevHub.
     * @param tpr       The preset econder ticks per revolution of the motor.
     */
    public XMotor(OpMode op, String motorPath, long tpr) {
        this.op = op;
        this.motorPath = motorPath;
        min = Integer.MIN_VALUE;
        max = Integer.MAX_VALUE;
        this.tpr = tpr;
    }

    /**
     * XMotor Constructor
     * <p>
     * Implements DcMotor with encoders with set range more efficiently.
     *
     * @param op        The OpMode in which the motor runs.
     * @param motorPath The name the motor is configured to through the RevHub.
     * @param min       The minimum encoder position the motor may run to.
     * @param max       The maximum encoder position the motor may run to.
     */
    public XMotor(OpMode op, String motorPath, int max, int min) {
        this.op = op;
        this.motorPath = motorPath;
        this.max = max;
        this.min = min;
    }

    /**
     * Initializes the motor by mapping and resetting the motor and encoders.
     */
    public void init() {
        motor = op.hardwareMap.dcMotor.get(motorPath);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reset();
    }

    /**
     * Sets the power of the motor without changing the mode.
     *
     * @param power The new power for the motor to run at.
     */
    public void setPower(double power) {
        this.power = power;
        rpmStopWatch.startTimer(100);
        motor.setPower(power);
    }

    //Stops the motor; sets power to 0, stops timer, and changes mode to indefinite

    /**
     * Stops the motor.
     */
    public void stop() {
        power = 0;
        rpmStopWatch.clearTimer();
        setIndefiniteRotation(0);
    }

    /**
     * Toggles the motor's brake system.
     */
    public void toggleBrakes() {
        brakes = !brakes;
        if (brakes) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /**
     * Sets the motor's brake system.
     *
     * @param brakes State to set the brake system to.
     */
    public void setBrakes(boolean brakes) {
        this.brakes = brakes;
        if (brakes) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } else {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    /**
     * Toggles the motor's safety system.
     * <p>
     * When a motor is "safe", it will power off when it faces severe resistance. Each motor is safe by default.
     */
    public void toggleSafe() {
        safe = !safe;
    }

    /**
     * Sets the motor's safety system.
     * <p>
     * When a motor is "safe", it will power off when it faces severe resistance. Each motor is safe by default.
     *
     * @param safe State to set the safety system to.
     */
    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    /**
     * Sets the encoder ticks per revolution of the motor.
     * <p>
     * See manufacturer data for values.
     *
     * @param tpr The encoder ticker per revolution of the motor.
     */
    public void setTPR(long tpr) {
        this.tpr = tpr;
    }

    /**
     * Sets the coefficient used to rotate a motor at a given rpm and disables automatic tuning.
     *
     * @param rpmCoef The motor's RPM coefficient.
     */
    public void setRPMCoefficient(double rpmCoef) {
        rpmCoefSet = true;
        this.rpmCoef = rpmCoef;
    }

    /**
     * Sets the threshold rpm before the safety system cuts the motor off.
     *
     * @param rpm The revolutions per minute to set the safety rpm to.
     */
    public void setSafeRPM(double rpm) {
        safeRPM = rpm;
    }

    /**
     * Sets the motor to a given RPM.
     * <p>
     * Note: requires that the tpr coefficient be tuned or preset.
     *
     * @param rpm The revolutions per minute to set the motor to.
     */
    public void setRPM(double rpm) {
        this.rpm = rpm;
        setPower(rpm * rpmCoef);
    }

    /**
     * Sets the motor to rotate indefinitely.
     */
    public void setIndefiniteRotation() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        cancelTimer();
        fixed = false;
    }

    /**
     * Sets the motor to rotate indefinitely at a given power.
     *
     * @param power Power to set the motor to.
     */
    public void setIndefiniteRotation(double power) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        cancelTimer();
        fixed = false;

        setPower(power);
    }

    /**
     * Sets the motor to rotate for a given duration
     *
     * @param milliseconds Duration, in milliseconds, for the motor to rotate.
     */
    public void setTimedRotation(int milliseconds) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        stopWatch.startTimer(milliseconds);
        fixed = false;
    }

    /**
     * Sets the motor to rotate for a given duration at a given power.
     *
     * @param milliseconds Duration, in milliseconds, for the motor to rotate.
     * @param power        Power to set the motor to.
     */
    public void setTimedRotation(int milliseconds, double power) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        stopWatch.startTimer(milliseconds);
        fixed = false;

        setPower(power);
    }

    /**
     * Sets the motor to rotate to a given position.
     *
     * @param targetPosition The target position of the motor.
     */
    public void setFixedRotation(int targetPosition) {
        this.targetPosition = targetPosition;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fixed = true;
    }

    /**
     * Sets the motor to rotate to a given position at a given power.
     *
     * @param targetPosition The target position of the motor.
     * @param power          Power to set the motor to.
     */
    public void setFixedRotation(int targetPosition, double power) {
        this.targetPosition = targetPosition;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fixed = true;
        setPower(power);
    }

    /**
     * Sets the motor to rotate to a given encoder position and remain there.
     *
     * @param targetPosition The target position of the motor
     */
    public void setPosition(int targetPosition) {
        this.targetPosition = targetPosition;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /**
     * Sets the motor to rotate at a given power to a given encoder position and remain there.
     *
     * @param targetPosition The target position of the motor
     * @param power          Power to set the motor to.
     */
    public void setPosition(int targetPosition, double power) {
        this.targetPosition = targetPosition;
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setPower(power);
    }

    /**
     * Cancels any scheduled stop of the motor.
     */
    public void cancelTimer() {
        stopWatch.clearTimer();
    }

    /**
     * Resets the motor's encoders.
     */
    public void reset() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Refreshes the motor's encoder position value.
     */
    public void refreshPosition() {
        lastPos = position;
        position = motor.getCurrentPosition();
    }

    /**
     * Rotates the motor by a given amount of encoder ticks.
     *
     * @param incrementAmount Amount of encoder ticks to rotate by.
     */
    public void increment(int incrementAmount) {
        refreshPosition();
        setFixedRotation(position + incrementAmount);
    }

    /**
     * Sets the minimum position of the motor.
     *
     * @param min The minimum encoder position of the motor.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the maximum position of the motor.
     *
     * @param max The maximum encoder position of the motor.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * The loop method of the motor;
     * Refreshes the motor's encoder variable.
     * Ensures motor within given encoder range.
     * Stops motors when scheduled to do so.
     * Tunes the rpm coefficient.
     */
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
            return;
        }
        //If motor isn't moving
        if (fixed && !motor.isBusy()) {
            stop();
            return;
        }

        if (power > 0.15 && ((!rpmCoefSet && rpmStopWatch.timerDone()) || safe)) {
            final int tickDiff = Math.abs(position - lastPos);
            final double r = (double) tickDiff / tpr;
            final long timeDiff = rpmStopWatch.elapsedMillis() * 1000 * 60;
            final double currentRPM = r / timeDiff;

            if (safe) {
                if (currentRPM < safeRPM * power) {
                    stop();
                    return;
                }
            }
            //If rpmc not set and motor is moving fast
            if (!rpmCoefSet && rpmStopWatch.timerDone()) {
                rpmCoef = power / currentRPM;
                rpmStopWatch.reset();
            }
        }
    }
}