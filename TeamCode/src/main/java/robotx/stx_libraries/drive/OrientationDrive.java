package robotx.stx_libraries.drive;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import robotx.stx_libraries.XModule;

/**
 * OrientationDrive Class
 * <p>
 * Custom class by FTC Team 4969 RobotX for better control of driving using device IMU.
 * <p>
 * Created by Nicholas on 11/3/16.
 */
public class OrientationDrive extends XModule {
    /**
     * Four wheel Driver-Centric-Drive drive stencil module.
     *
     * @param op The opMode the OrientationDrive Module will be in.
     */
    public OrientationDrive(OpMode op) {
        super(op);
    }

    /**
     * The front-left motor of the drive train.
     */
    public DcMotor frontLeft;
    /**
     * The front-right motor of the drive train.
     */
    public DcMotor frontRight;
    /**
     * The back-right motor of the drive train.
     */
    public DcMotor backRight;
    /**
     * The back-left motor of the drive train.
     */
    public DcMotor backLeft;

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
    private double joystickAngle;

    private double x;
    private double y;
    private double r;

    /**
     * Toggle on whether or not orientation mode is active.
     */
    public boolean orientationMode = true;
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
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft");
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight");
        backRight = opMode.hardwareMap.dcMotor.get("backRight");
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft");

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        gyroSensor = opMode.hardwareMap.get(BHI260IMU.class, "imu");
        gyroSensor.initialize();
    }

    /**
     * Toggles whether or not orientation mode is active.
     */
    public void toggleOrientation() {
        orientationMode = !orientationMode;
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
        x = xGamepad1.left_stick_x;
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
        final double s = ((Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(r)))) * (Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(r))))) / ((x * x) + (y * y) + (r * r));

        final double xPrime = (Math.sqrt((x * x) + (y * y))) * (Math.cos(robotAngle + joystickAngle));
        final double yPrime = -(Math.sqrt(((x * x) + (y * y)))) * (Math.sin(robotAngle + joystickAngle));

        final double flPow = (yPrime - xPrime - r) * (s) * power;
        final double frPow = (yPrime + xPrime + r) * (s) * power;
        final double brPow = (yPrime - xPrime + r) * (s) * power;
        final double blPow = (yPrime + xPrime - r) * (s) * power;

        frontLeft.setPower(flPow);
        frontRight.setPower(frPow);
        backLeft.setPower(blPow);
        backRight.setPower(brPow);
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
     * Sets the robot to strafe at a given power.
     *
     * @param power The power to strafe the robot at; +: right
     */
    public void strafe(double power){
        x = power;
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

    public void stop(){
        x = 0;
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

        if (orientationMode) {
            robotAngle = Math.toRadians(globalAngle - offset);
        } else {
            robotAngle = 0;
        }

        // Position of joystick when not straight forward
        if (x > 0) {
            joystickAngle = Math.atan(-y / x);
        } else if (x < 0) {
            joystickAngle = Math.atan(-y / x) + Math.toRadians(180);
        }
        // Position of joystick when near perfectly forward
        else if (y > 0) {
            joystickAngle = Math.toRadians(270);
        } else if (y < 0) {
            joystickAngle = Math.toRadians(90);
        }

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