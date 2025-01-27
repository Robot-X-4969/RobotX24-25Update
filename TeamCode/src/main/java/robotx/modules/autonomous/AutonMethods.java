package robotx.modules.autonomous;

import static robotx.modules.opmode.LiftSystem.margin;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import robotx.libraries.XModule;
import robotx.libraries.XServo;


public class AutonMethods extends XModule {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public DcMotor liftMotor1;
    public DcMotor liftMotor2;

    public XServo[] liftServos;

    public static final double tileTime = 0;
    public static final double liftTime = 0;

    //methods are built into one button as a toggle

    public AutonMethods(OpMode op) {
        super(op);
        liftServos = new XServo[]{
                new XServo(op, "liftServo1", new double[]{
                        .75 + margin, .25 + margin, .25 + margin, .75 + margin
                }),
                new XServo(op, "liftServo2", new double[]{
                        .25 - margin, .75 - margin, .75 - margin, .25 - margin
                }),
                new XServo(op, "liftServo3", new double[]{
                        .75 + margin, .25 + margin, .25 + margin, .75 + margin
                }),
                new XServo(op, "liftServo4", new double[]{
                        .25 - margin, .75 - margin, .75 - margin, .25 - margin
                })
        };
    }
    public void init() {
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE); //WHEN DOM TRAIN
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight");
        //frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight = opMode.hardwareMap.dcMotor.get("backRight");
        //backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);  //WHEN DOM TRAIN

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftMotor1 = opMode.hardwareMap.dcMotor.get("liftMotor1");
        liftMotor2 = opMode.hardwareMap.dcMotor.get("liftMotor2");
    }

    public int sleepTime (double t) {
        return (int) Math.floor(t);
    }

    public void driveForward(double power) {
        frontLeft.setPower(power);  //top left when rev is down and ducky wheel is right
        frontRight.setPower(power); //bottom left
        backLeft.setPower(power);   //top right
        backRight.setPower(power); // bottom right
    }
    public void driveStop() {
        frontLeft.setPower(0);  //top left when rev is down and ducky wheel is right
        frontRight.setPower(0); //bottom left
        backLeft.setPower(0);   //top right
        backRight.setPower(0); // bottom right
    }
    public void driveBackward(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(-power);
    }
    public void strafeRight(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void strafeLeft(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }
    public void diagonalLeft(double power){
        frontRight.setPower(power);
        backLeft.setPower(power);
    }
    public void diagonalRight(double power){
        frontLeft.setPower(power);
        backRight.setPower(power);
    }
    public void turnLeft(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }
    public void turnRight(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public void lift(double power){
        liftMotor1.setPower(-power);
        liftMotor2.setPower(-power);
    }

    public void stopLift(){
        liftMotor1.setPower(0);
        liftMotor2.setPower(0);
    }
}
