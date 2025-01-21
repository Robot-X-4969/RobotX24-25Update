package robotx.modules.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import robotx.libraries.XModule;
import robotx.libraries.XServo;

public class LiftSystem extends XModule {

    // motors being used

    public static boolean toggle = true;

    private final XServo[] liftServos;

    public double power = 1;
    public double margin = 0.0;

    public DcMotor liftMotor1;
    public DcMotor liftMotor2;

    public boolean lifted = true;

    public LiftSystem(OpMode op) {
        super(op);
        liftServos = new XServo[]{
                new XServo(op, "liftServo1", new double[]{
                        .75+margin, .25+margin, .75+margin
                }),
                new XServo(op, "liftServo2", new double[]{
                        .25-margin, .75-margin, .25-margin
                }),
                new XServo(op, "liftServo3", new double[]{
                        .75+margin, .25+margin, .75+margin
                }),
                new XServo(op, "liftServo4", new double[]{
                        .25-margin, .75-margin, .25-margin
                })
        };
    }
    public void init() {
        for(XServo servo : liftServos){
            servo.init();
        }
        liftMotor1 = opMode.hardwareMap.dcMotor.get("liftMotor1");
        liftMotor2 = opMode.hardwareMap.dcMotor.get("liftMotor2");
    }

    // sets lift motor power one to the opposite of lift motor one because that's what makes them work
    public void raiseLift(double liftPower) {
        liftMotor1.setPower(liftPower);
        liftMotor2.setPower(liftPower);
    }

    public void moveLift() {
        for(XServo servo : liftServos){
            servo.forward();
        }
    }


    public void loop() {
        if (toggle) {
            if (xGamepad1().a.wasPressed()) {
                moveLift();
            }
            if (xGamepad1().dpad_up.isDown()) {
                raiseLift(-power);
            } else if (xGamepad1().dpad_down.isDown()) {
                raiseLift(power);
            } else {
                raiseLift(0);
            }
        } else {
            if (xGamepad2().a.wasPressed()) {
                moveLift();
            }
            if (xGamepad2().dpad_up.isDown()) {
                raiseLift(-power);
            } else if (xGamepad2().dpad_down.isDown()) {
                raiseLift(power);
            } else {
                raiseLift(0);
            }
        }
    }
}