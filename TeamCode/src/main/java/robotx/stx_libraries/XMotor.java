package robotx.stx_libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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

    private final Stopwatch stopWatch = new Stopwatch();

    private boolean brakes = true;
    private boolean reverse = false;
    private boolean safe = false;
    private double safeRPM = 100;

    private long tpr = 1500;
    private double rpm = 0;

    private double rpmCoef = 0;
    private boolean rpmCoefSet = false;
    private final Stopwatch rpmStopwatch = new Stopwatch(100);
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
        safe = true;
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
        if (power > 1) {
            power = 1;
        } else if (power < -1) {
            power = -1;
        }
        this.power = power;
        rpmStopwatch.startTimer(100);
        motor.setPower(power);
    }

    //Stops the motor; sets power to 0, stops timer, and changes mode to indefinite

    /**
     * Stops the motor.
     */
    public void stop() {
        power = 0;
        rpmStopwatch.clearTimer();
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
     * Toggle's the motor's direction.
     */
    public void toggleDirection(){
        reverse = !reverse;
        if(reverse){
            motor.setDirection(DcMotorSimple.Direction.REVERSE);
        } else {
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
    }

    /**
     * Sets the motor's direction.
     * @param direction Direction to set the motor to; true: forwards, false: reverse
     */
    public void setDirection(boolean direction){
        reverse = !direction;
        if(reverse){
            motor.setDirection(DcMotorSimple.Direction.REVERSE);
        } else {
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
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
     * Sets the motor to rotate for a given duration through a given scheduler.\
     * <p>
     * Event is scheduled under ID "{motorPath}Stop".
     *
     * @param milliseconds Duration, in milliseconds, for the motor to rotate.
     * @param scheduler    The Scheduler object to schedule the motor's stop through.
     */
    public void setTimedRotation(Scheduler scheduler, int milliseconds) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        scheduler.schedule(milliseconds, motorPath+"Stop", this::stop);
    }

    /**
     * Sets the motor to rotate for a given duration at a given power through a given scheduler.\
     * <p>
     * Event is scheduled under ID "{motorPath}Stop".
     *
     * @param milliseconds Duration, in milliseconds, for the motor to rotate.
     * @param power The power to set the motor to.
     * @param scheduler    The Scheduler object to schedule the motor's stop through.
     */
    public void setTimedRotation(Scheduler scheduler, int milliseconds, double power) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setPower(power);
        scheduler.schedule(milliseconds, motorPath+"Stop", this::stop);
    }

    /**
     * Cancels any scheduled stop of the motor.
     */
    public void cancelTimer() {
        stopWatch.clearTimer();
    }

    /**
     * Cancels any scheduled stop of the motor in a given Scheduler.
     *
     * @param scheduler The Scheduler to cancel the timer within.
     */
    public void cancelTimer(Scheduler scheduler) {
        scheduler.cancel(motorPath+"Stop");
    }

    /**
     * Resets the motor's encoders.
     */
    public void reset() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        position = 0;
        lastPos = 0;
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
     * Checks if the motor has reached its target encoder position.
     *
     * @return True if with 1 degree of target position; false if else
     */
    public boolean atTarget(){
        refreshPosition(false);
        return Math.abs(targetPosition - position) < tpr/360;
    }

    /**
     * Holds thread until motor reaches target encoder position.
     *
     * @throws InterruptedException If Thread or loop fails.
     */
    public void awaitTarget() throws InterruptedException {
        while(!atTarget()){
            Thread.sleep(25);
        }
    }

    /**
     * Holds thread until motor reaches target encoder position or until given duration elapses.
     *
     * @param failTime The time, in milliseconds, until it stops holding the thread.
     * @throws InterruptedException If Thread or loop fails.
     */
    public void awaitTarget(int failTime) throws InterruptedException {
        final Stopwatch stopwatch = new Stopwatch(failTime);
        while(!atTarget() && !stopwatch.timerDone()){
            Thread.sleep(25);
        }
    }

    /**
     * Rotates the motor by a given amount of encoder ticks.
     *
     * @param incrementAmount Amount of encoder ticks to rotate by.
     */
    public void increment(int incrementAmount) {
        refreshPosition(false);
        setFixedRotation(position + incrementAmount);
    }

    /**
     * Rotates the motor by a given amount of encoder ticks at a given power.
     *
     * @param incrementAmount Amount of encoder ticks to rotate by.
     * @param power The power to set the motor to.
     */
    public void increment(int incrementAmount, double power) {
        refreshPosition(false);
        setFixedRotation(position + incrementAmount, power);
    }

    /**
     * Sets the motor's position a certain amount of ticks forward.
     *
     * @param incrementAmount Amount of encoder ticks to rotate by.
     */
    public void incrementPosition(int incrementAmount) {
        refreshPosition(false);
        setPosition(position + incrementAmount);
    }

    /**
     * Sets the motor's position a certain amount of ticks forward at a given power.
     *
     * @param incrementAmount Amount of encoder ticks to rotate by.
     * @param power The power to set the motor to.
     */
    public void incrementPosition(int incrementAmount, double power) {
        refreshPosition(false);
        setPosition(position + incrementAmount, power);
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
     * Sets the minimum and maximum position of the motor.
     *
     * @param min The minimum encoder position of the motor.
     * @param max The maximum encoder position of the motor.
     */
    public void setRange(int min, int max) {
        this.max = max;
    }

    /**
     * Refreshes the motor's encoder position value.
     *
     * @param milestone Whether or not this is a milestone, where the lastPosition is updated.
     */
    public void refreshPosition(boolean milestone) {
        if (milestone) {
            lastPos = position;
        }
        position = motor.getCurrentPosition();
    }

    /**
     * The loop method of the motor;
     * Refreshes the motor's encoder variable.
     * Ensures motor within given encoder range.
     * Stops motors when scheduled to do so.
     * Tunes the rpm coefficient.
     */
    public void loop() {
        refreshPosition(false);

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

        if (Math.abs(power) > 0.15 && rpmStopwatch.timerDone() && (!rpmCoefSet || safe)) {
            final int tickDiff = Math.abs(position - lastPos);
            final double r = ((double) tickDiff) / tpr;
            final long timeDiff = rpmStopwatch.elapsedMillis();
            final double currentRPM = r / ((double) timeDiff) * 1000 * 60;

            rpmStopwatch.reset();
            refreshPosition(true);
            if (safe) {
                if (currentRPM < safeRPM * Math.abs(power)) {
                    stop();
                    return;
                }
            }
            if (!rpmCoefSet) {
                rpmCoef = power / currentRPM;
            }
        }
    }
}