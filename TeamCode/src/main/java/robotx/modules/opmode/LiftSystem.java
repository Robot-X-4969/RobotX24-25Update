package robotx.modules.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.stx_libraries.XModule;
import robotx.stx_libraries.XMotor;
import robotx.stx_libraries.XServo;

public class LiftSystem extends XModule {

    // motors being used
    public static int state = 0;
    public static boolean canTurn = false;
    private static int range = 2150;

    private final XServo[] liftServos;

    public static double margin = 0.0;

    private final XMotor[] liftMotors;

    public LiftSystem(OpMode op) {
        super(op);

        liftMotors = new XMotor[]{
                new XMotor(op, "liftMotor1"),
                new XMotor(op, "liftMotor2"),
        };

        liftServos = new XServo[]{
                new XServo(op, "liftServo1", new double[]{
                        .75 + margin, .25 + margin, .25 + margin, .25 + margin, .75 + margin,
                }),
                new XServo(op, "liftServo2", new double[]{
                        .25 - margin,.75 - margin, .75 - margin, .75 - margin, .25 - margin,
                }),
                new XServo(op, "liftServo3", new double[]{
                        .75 + margin, .25 + margin, .25 + margin, .25 + margin, .75 + margin,
                }),
                new XServo(op, "liftServo4", new double[]{
                        .25 - margin, .75 - margin, .75 - margin, .75 - margin, .25 - margin,
                })
        };
    }

    @Override
    public void init() {
        double sign = 1;
        for (XServo servo : liftServos) {
            servo.init();
            servo.setPosition(.5 + sign * (margin + .05));
            sign *= -1;
        }
        for(XMotor motor : liftMotors){
            motor.init();
            loopMotors.add(motor);
        }
    }

    public void incrementState() {
        state = liftServos[0].getIndex();
    }

    // sets lift motor power one to the opposite of lift motor one because that's what makes them work
    public void lift(double liftPower) {
        liftMotors[0].setPower(liftPower);
        liftMotors[1].setPower(-liftPower);
    }

    public void rotateLift(boolean forward) {
        if(canTurn) {
            if (forward) {
                for (XServo servo : liftServos) {
                    servo.forward();
                }
                incrementState();
            } else {
                for (XServo servo : liftServos) {
                    servo.backward();
                }
                incrementState();
            }
            if (state == 0 || state == 4) {
                liftMotors[1].setRange(0, Integer.MAX_VALUE);
                liftMotors[0].setRange(Integer.MIN_VALUE, 0);
            } else {
                liftMotors[1].setRange(0, range);
                liftMotors[0].setRange(-range, 0);
            }
        }
    }

    @Override
    public void control_loop() {
        canTurn = Math.abs(liftMotors[1].getPosition()) < 500;
        // 2nd Driver Controls
        if (dualPlayer) {
            if (xGamepad2.a.wasPressed()) {
                rotateLift(true);
            }
            if (xGamepad2.x.wasPressed()) {
                rotateLift(false);
            }
            if (xGamepad1.right_trigger > .1) {
                lift(-xGamepad1.right_trigger);
            } else if (xGamepad1.left_trigger > .1) {
                lift(xGamepad1.left_trigger);
            } else if (xGamepad2.right_trigger > .1) {
                lift(-xGamepad2.right_trigger);
            } else if (xGamepad2.left_trigger > .1) {
                lift(xGamepad2.left_trigger);
            } else {
                lift(0);
            }
        } else {
            if (xGamepad1.right_trigger > .1) {
                lift(-xGamepad1.right_trigger);
            } else if (xGamepad1.left_trigger > .1) {
                lift(xGamepad1.left_trigger);
            } else {
                lift(0);
            }
        }
        // 1st Driver Controls; Overrides
        if (xGamepad1.a.wasPressed()) {
            rotateLift(true);
        }
    }
}