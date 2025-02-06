package robotx.stx_libraries.drive;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import robotx.stx_libraries.Stopwatch;
import robotx.stx_libraries.XModule;

/**
 * TankOrientationDrive Class
 * <p>
 * Custom class by FTC Team 4969 RobotX for better control of driving using device IMU.
 * <p>
 * Created by John Daniher on 2/5/2025.
 */
public class TankOrientationDrive extends XModule {
    /**
     * Two wheel drive stencil module.
     *
     * @param op The opMode the TankOrientationDrive Module will be in.
     */
    public TankOrientationDrive(OpMode op) {
        super(op);
        motorsPerSide = 1;
        leftMotors = new DcMotor[1];
        rightMotors = new DcMotor[1];
    }

    /**
     * Two wheel drive stencil module with a given amount of motors per side.
     *
     * @param op The opMode the TankOrientationDrive Module will be in.
     * @param motorsPerSide The amount of motors per side of the drive train.
     */
    public TankOrientationDrive(OpMode op, int motorsPerSide) {
        super(op);
        this.motorsPerSide = motorsPerSide;
        leftMotors = new DcMotor[motorsPerSide];
        rightMotors = new DcMotor[motorsPerSide];
    }


    /**
     * The number of motors per side of the robot's drive train.
     */
    final int motorsPerSide;

    /**
     * An array of all motors on the left side.
     */
    public final DcMotor[] leftMotors;

    /**
     * An array of all motors on the right side.
     */
    public final DcMotor[] rightMotors;

    private BHI260IMU gyroSensor;
    private Orientation lastAngles = new Orientation();
    /**
     * The angle at which the robot is currently rotated in respect to the field (globe)
     */
    public double globalAngle;
    /**
     * The angle at which the robot is currently rotated in respect to its set orientation angle.
     */
    public double robotAngle;

    private double y;
    private double r;

    private double offset = 0;

    /**
     * Toggle on whether or not slow mode is active.
     * <p>
     * If active, power = 0.5.
     */
    public boolean slowMode = false;
    /**
     * Toggle on whether or not super slow mode is active.
     * <p>
     * If active, power = 0.2.
     */
    public boolean superSlowMode = false;

    /**
     * The current percent power of the motors, ranging -1 to 1.
     */
    public double power = 0.75;

    /**
     * Initialization function. This method, by default, initializes the motors and gyroSensor
     */
    @Override
    public void init() {
        if(motorsPerSide > 1){
            for(int i = 0; i < leftMotors.length; i++){
                leftMotors[i] = opMode.hardwareMap.dcMotor.get("leftMotor" + i);
            }
            for(int i = 0; i < rightMotors.length; i++){
                rightMotors[i] = opMode.hardwareMap.dcMotor.get("rightMotor" + i);
            }
        } else {
            leftMotors[0] = opMode.hardwareMap.dcMotor.get("leftMotor");
            rightMotors[0] = opMode.hardwareMap.dcMotor.get("rightMotor");
        }

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        gyroSensor = opMode.hardwareMap.get(BHI260IMU.class, "imu");
        gyroSensor.initialize();
    }

    /**
     * Toggles slow mode.
     * <p>
     * When slow mode is active, power = 0.5.
     */
    public void toggleSlow() {
        slowMode = !slowMode;
        superSlowMode = false;
    }

    /**
     * Toggles super slow mode.
     * <p>
     * When super slow mode is active, power = 0.2;
     */
    public void toggleSuperSlow() {
        slowMode = false;
        superSlowMode = !superSlowMode;
    }

    /**
     * Resets the saved orientation of the gyroSensor.
     */
    public void resetOrientation() {
        offset = globalAngle;
    }

    /**
     * Refreshes the variables tracking the joystick movements
     */
    public void refreshStick() {
        y = xGamepad1.left_stick_y;
        r = xGamepad1.right_stick_x;
    }

    /**
     * Gets the current angle at which the robot is rotated at in respect to the field, then refreshes angle variables.
     *
     * @return The angle at which the robot is rotated at.
     */
    public double getHeadingAngle() {
        Orientation angles = gyroSensor.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180) {
            deltaAngle += 360;
        } else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }
        // Change in angle compared to orientation: if gyro measures clockwise should add rather than subtract
        globalAngle -= deltaAngle;
        lastAngles = angles;

        return globalAngle;
    }

    /**
     * Calculates and sets the power of each motor.
     *
     * @param power The percent power, ranging -1 to 1, to power each motor to.
     */
    public void powerMotors(double power){
        final double lPow = (y+r)*power;
        final double rPow = (y-r)*power;

        for(DcMotor motor : leftMotors){
            motor.setPower(lPow);
        }
        for(DcMotor motor : rightMotors){
            motor.setPower(rPow);
        }
    }

    /**
     * Sets the robot to drive at a given power.
     *
     * @param power The power to drive the robot at; +: forwards
     */
    public void drive(double power){
        y = power;
        powerMotors(1);
    }

    /**
     * Sets the robot to rotate at a given power.
     *
     * @param power The power to rotate the robot at; +: clockwise
     */
    public void rotate(double power) {
        r = power;
        powerMotors(1);
    }

    /**
     * Rotates the robot at a given power until the IMU reads a given angle or 3 seconds has elapsed.
     *
     * @param power The power, ranging -1 to 1, to rotate the robot at.
     * @param angle The angle to rotate the robot to.
     * @throws InterruptedException On Thread Interrupted, loop fails.
     */
    public void rotateToAngle(double power, double angle) throws InterruptedException {
        final Stopwatch stopwatch = new Stopwatch(3000);
        while(!stopwatch.timerDone()){
            robotAngle = getHeadingAngle() - offset;

            double margin = angle-robotAngle;
            if(margin < -180) {
                margin += 360;
            } else if (margin > 180) {
                margin -= 360;
            }

            if(Math.abs(margin) < 1){
                return;
            }

            r = Math.signum(margin) * Math.min(45, Math.abs(margin)) / 45;

            powerMotors(1);
            //Give the CPU some rest
            Thread.sleep(10);
        }
    }

    /**
     * Rotates the robot at a given power until the IMU reads a given angle or a given duration has elapsed.
     *
     * @param power The power, ranging -1 to 1, to rotate the robot at.
     * @param angle The angle to rotate the robot to.
     * @param failTime The duration, in milliseconds, to attempt the rotate the robot for.
     * @throws InterruptedException On Thread Interrupted, loop fails.
     */
    public void rotateToAngle(double power, double angle, int failTime) throws InterruptedException {
        final Stopwatch stopwatch = new Stopwatch(failTime);
        while(!stopwatch.timerDone()){
            robotAngle = getHeadingAngle() - offset;

            double margin = angle-robotAngle;
            if(margin < -180) {
                margin += 360;
            } else if (margin > 180) {
                margin -= 360;
            }

            if(Math.abs(margin) < 1){
                return;
            }

            r = Math.signum(margin) * power * Math.min(45, Math.abs(margin)) / 45;

            powerMotors(1);
            //Give the CPU some rest
            Thread.sleep(10);
        }
    }

    public void stopMotors(){
        y = 0;
        r = 0;
        powerMotors(1);
    }

    /**
     * Loop method which runs while opMode is active.
     * <p>
     * Runs getHeadingAngle(), calculates and powers motors.
     */
    @Override
    public void loop() {
        super.loop();
        getHeadingAngle();
        robotAngle = Math.toRadians(globalAngle - offset);

        if (power > 1) {
            power = 1;
        }
        if (power < 0.25) {
            power = 0.25;
        }

        // Temporary power variable
        double pow = power;

        if (superSlowMode) {
            pow = 0.2;
        } else if (slowMode) {
            pow = 0.5;
        }

        powerMotors(pow);
    }
}