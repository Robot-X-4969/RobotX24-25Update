package robotx.modules.opmode;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import robotx.libraries.XModule;

public class OrientationDrive extends XModule {
    public OrientationDrive(OpMode op) {
        super(op);
    }


    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backRight;
    public DcMotor backLeft;

    //Gyro ID
    public BHI260IMU gyroSensor;
    //public BNO055IMU gyroSensor;
    public Orientation lastAngles = new Orientation();
    public double globalAngle;
    public double robotAngle;
    public double joystickAngle;

    public double x;
    public double y;
    public double s;
    public double r;


    public double xPrime;
    public double yPrime;

    public boolean orientationMode = true;
    public double offset = 0;

    //public double dash = 10;  //for soccer robot

    public boolean slowMode = false;
    public boolean superSlowMode = false;

    double flPow = ((yPrime - xPrime + r) * (-s));
    double frPow = ((yPrime + xPrime - r) * (-s));
    double brPow = ((yPrime - xPrime - r) * (-s));
    double blPow = ((yPrime + xPrime + r) * (-s));

    double power = 0.5;

    public void init() {
//Reverses Motors
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE); //WHEN DOM TRAIN
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight");
        //frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight = opMode.hardwareMap.dcMotor.get("backRight");
        //backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);  //WHEN DOM TRAIN


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        gyroSensor = opMode.hardwareMap.get(BHI260IMU.class, "gyroSensor");
        //gyroSensor = opMode.hardwareMap.get(BHI260IMU.class, "gyroSensor");
        gyroSensor.initialize();

    }

    public int getHeadingAngle() {
        Orientation angles = gyroSensor.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //Orientation angles = gyroSensor.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180) {
            deltaAngle += 360;
        }
        else if (deltaAngle > 180) {
            deltaAngle -= 360;
        }
//Change in angle compared to orientation: if gyro measures clockwise should add rather than subtract
        globalAngle -= deltaAngle;

        int finalAngle = (int) globalAngle;

        lastAngles = angles;

        return finalAngle;
    }

    public void switchMode() {
        orientationMode = !orientationMode;
    }

    public void toggleSlow() {
        if (slowMode) {
            slowMode = false;
        } else if (superSlowMode) {
            superSlowMode = false;
            slowMode = true;
        } else {
            slowMode = true;
        }
    }

    public void toggleSuperSlow() {
        if (superSlowMode) {
            superSlowMode = false;
        } else if (slowMode) {
            slowMode = false;
            superSlowMode = true;
        } else {
            superSlowMode = true;
        }
    }

    public void loop() {

        getHeadingAngle();


        if (orientationMode) {
            robotAngle = Math.toRadians(globalAngle - offset);
        } else {
            robotAngle = 0;
        }
        /*if (xGamepad1().y.wasPressed()) {
            switchMode();
        }
        */
        if (xGamepad1().x.wasPressed()) {
            offset = globalAngle;
        }
        //opMode.telemetry.addData("Orientation mode:", orientationMode);


        x = xGamepad1().left_stick_x;
        y = xGamepad1().left_stick_y;
//Position of right stick AKA direction robot should turn
        r = xGamepad1().right_stick_x;
        s = ((Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(r)))) * (Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(r))))) / ((x * x) + (y * y) + (r * r));


//Position of joystick when not straight forward
        int jsOffset = 90;
        if (x > 0) {
            joystickAngle = Math.atan(-y / x) + Math.toRadians(270 + jsOffset);
        } else if (x < 0) {
            joystickAngle = Math.atan(-y / x) + Math.toRadians(90 + jsOffset);
        }
//Position of joystick when near perfectly forward
        else if (y > 0) {
            joystickAngle = Math.toRadians(180 + jsOffset);
        } else if (y < 0) {
            joystickAngle = Math.toRadians(360 + jsOffset);
        }

        xPrime = (Math.sqrt((x * x) + (y * y))) * (Math.cos(robotAngle + joystickAngle));
        yPrime = -(Math.sqrt((x * x + y * y))) * (Math.sin(robotAngle + joystickAngle));

        //Makes robot slower when picking up blocks
        superSlowMode = ClawSystem.state == 1;
        /*
        if(xGamepad1().dpad_left.wasPressed()){
            power -= 0.25;
        }
        if(xGamepad1().dpad_right.wasPressed()){
            power += 0.25;
        }
         */
        if(power > 1){
            power = 1;
        }
        if(power < 0.25){
            power = 0.25;
        }

        if (slowMode) {
            frontLeft.setPower((yPrime - xPrime - r) * (s) * 0.55);
            backRight.setPower((yPrime - xPrime + r) * (s) * 0.55);

            frontRight.setPower((yPrime + xPrime + r) * (s) * 0.55);
            backLeft.setPower((yPrime + xPrime - r) * (s) * 0.55);
        } else if (superSlowMode) {
            frontLeft.setPower((yPrime - xPrime - r) * (s) * 0.33);
            backRight.setPower((yPrime - xPrime + r) * (s) * 0.33);

            frontRight.setPower((yPrime + xPrime + r) * (s) * .33);
            backLeft.setPower((yPrime + xPrime - r) * (s) * .33);
        } else {
            frontLeft.setPower((yPrime - xPrime - r) * (s) * power);
            backRight.setPower((yPrime - xPrime + r) * (s) * power);

            frontRight.setPower((yPrime + xPrime + r) * (s) * power);
            backLeft.setPower((yPrime + xPrime - r) * (s) * power);
        }

        // deadzone code
    /*
        if ( 0 < Math.abs(flPow) && Math.abs(flPow) < 0.4){
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
        else if ( 0 < Math.abs(frPow) && Math.abs(frPow) < 0.4){
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
        else if ( 0 < Math.abs(brPow) && Math.abs(brPow) < 0.4){
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
        else if ( 0 < Math.abs(blPow) && Math.abs(blPow) < 0.4){
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
        else{
            frontLeft.setPower(flPow);
            frontRight.setPower(frPow);
            backLeft.setPower(blPow);
            backRight.setPower(brPow);
        }
    */


    }
}