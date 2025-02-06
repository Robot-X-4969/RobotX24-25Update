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
 * OmniDrive Class
 * <p>
 * Custom class by FTC Team 4969 RobotX for control of robot with omni wheels.
 * <p>
 * Created by John Daniher on 2/5/2025.
 */
public class OmniDrive extends XModule {
    /**
     * Four wheel omni drive stencil module.
     *
     * @param op The opMode the OmniDrive Module will be in.
     */
    public OmniDrive(OpMode op) {
        super(op);
    }

    /**
     * The front motor of the drive train.
     */
    public DcMotor front;
    /**
     * The right motor of the drive train.
     */
    public DcMotor right;
    /**
     * The back motor of the drive train.
     */
    public DcMotor back;
    /**
     * The left motor of the drive train.
     */
    public DcMotor left;

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
     * Initialization function. This method, by default, initializes the motors and gyroSensor
     */
    @Override
    public void init() {
        front = opMode.hardwareMap.dcMotor.get("front");
        right = opMode.hardwareMap.dcMotor.get("right");
        back = opMode.hardwareMap.dcMotor.get("back");
        left = opMode.hardwareMap.dcMotor.get("left");
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
        final double s = Math.pow(Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(r))), 2) / ((x * x) + (y * y) + (r * r));

        final double xPow = (x + r) * (s) * power;
        final double yPow = (y + r) * (s) * power;

        front.setPower(xPow);
        right.setPower(yPow);
        back.setPower(xPow);
        left.setPower(yPow);
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

    public void stopMotors(){
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