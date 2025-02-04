package robotx.stx_libraries.drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import robotx.stx_libraries.XModule;

/**
 * MecanumDrive Class
 * <p>
 * Custom class by FTC Team 4969 RobotX for implementation of mecanum wheel drive.
 * <p>
 * Created by John Daniher on 2/4/2025.
 */
public class MecanumDrive extends XModule {
    /**
     * Four wheel mecanum drive stencil module.
     *
     * @param op The opMode the MecanumDrive Module will be in.
     */
    public MecanumDrive(OpMode op) {super(op);}

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

    private double x;
    private double y;
    private double r;

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
     * Initialization function. This method, by default, initializes the motors
     */
    @Override
    public void init() {
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft");
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight");
        backRight = opMode.hardwareMap.dcMotor.get("backRight");
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft");
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
     * Refreshes the variables tracking the joystick movements
     */
    public void refreshStick() {
        x = xGamepad1.left_stick_x;
        y = xGamepad1.left_stick_y;
        r = xGamepad1.right_stick_x;
    }

    /**
     * Calculates and sets the power of each motor.
     *
     * @param power The percent power, ranging -1 to 1, to power each motor to.
     */
    public void powerMotors(double power){
        final double s = ((Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(r)))) * (Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(r))))) / ((x * x) + (y * y) + (r * r));

        final double xPrime = (Math.sqrt((x * x) + (y * y)));
        final double yPrime = -(Math.sqrt((x * x) + (y * y)));

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
     * Calculates and powers motors.
     */
    @Override
    public void loop() {
        super.loop();

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