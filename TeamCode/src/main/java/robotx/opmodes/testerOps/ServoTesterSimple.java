package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import robotx.libraries.PressHandler;

/**
 * Created by Nicholas on 11/6/16.
 * heavily edited by cstaut 11/6/23 lol
 * Use to test the servo at any position.
 * Hold down the back bumpers to change the unit,
 * and press the up and down buttons on the D-Pad to increment/decrement.
 */

@TeleOp(name = "ServoTesterSimple", group = "Tests")
public class ServoTesterSimple extends OpMode {

    Servo testServo1;
    Servo testServo2;

    Servo leftArm;
    Servo rightArm;

    double servoPosition1;
    double servoPosition2;

    // code it so the servos will hold their position after switching groups
    //servoPosition[] = {group1Pos, group2Pos, group3Pos};
    //servoPosition[groupNumber - 1][1] = 2.0;

    PressHandler gamepad1_dpad_up;
    PressHandler gamepad1_dpad_down;
    PressHandler gamepad1_a;
    PressHandler gamepad1_b;
    PressHandler gamepad1_y;
    PressHandler gamepad1_left_bumper;
    PressHandler gamepad1_right_bumper;

    boolean scaleEnabled = false;

    double unit = 0.1;

    @Override
    public void init() {

        testServo1 = hardwareMap.servo.get("leftIntake"); //up: 0.491
        testServo2 = hardwareMap.servo.get("rightIntake"); //up: 0.670

        gamepad1_dpad_up = new PressHandler();
        gamepad1_dpad_down = new PressHandler();
        gamepad1_a = new PressHandler();
        gamepad1_b = new PressHandler();
        gamepad1_y = new PressHandler();
        gamepad1_left_bumper = new PressHandler();
        gamepad1_right_bumper = new PressHandler();
    }

    @Override
    public void start() {
        servoPosition1 = .261;
        servoPosition2 = .738;
    }

    @Override
    public void loop() {

        gamepad1_dpad_up.update(gamepad1.dpad_up);
        gamepad1_dpad_down.update(gamepad1.dpad_down);
        gamepad1_a.update(gamepad1.a);
        gamepad1_b.update(gamepad1.b);
        gamepad1_y.update(gamepad1.y);
        gamepad1_left_bumper.update(gamepad1.left_bumper);
        gamepad1_right_bumper.update(gamepad1.right_bumper);

        if (gamepad1_b.onPress()) {
            if (scaleEnabled) {
                testServo1.scaleRange(0.0, 1.0);
                testServo2.scaleRange(0.0, 1.0);
                scaleEnabled = false;
            } else {
                testServo1.scaleRange(0.02, 0.98);
                testServo2.scaleRange(0.02, 0.98);
                scaleEnabled = true;
            }
        }

        if (gamepad1_left_bumper.onPress()) {
            unit /= 10;
            if (unit <= 0.0001) {
                unit = 0.001;
            }
        }
        if (gamepad1_right_bumper.onPress()) {
            unit *= 10;
            if (unit >= 1) {
                unit = 0.1;
            }
        }
        telemetry.addData("Unit: ", unit);

        if (gamepad1_dpad_up.onPress()) {
            servoPosition1 += unit;
        }
        if (gamepad1_dpad_down.onPress()) {
            servoPosition1 -= unit;
        }
        if (gamepad1_y.onPress()) {
            servoPosition2 += unit;
        }
        if (gamepad1_a.onPress()) {
            servoPosition2 -= unit;
        }

        if (servoPosition1 > 1.0) {
            servoPosition1 = 1.0;
        }
        if (servoPosition2 > 1.0) {
            servoPosition2 = 1.0;
        }

        testServo1.setPosition(servoPosition1);
        testServo2.setPosition(servoPosition2);

        telemetry.addData("Scale Enabled?    ", scaleEnabled);
        telemetry.addData("Servo Position 1: ", testServo1.getPosition());
        telemetry.addData("Servo Position 2: ", testServo2.getPosition());
    }

    @Override
    public void stop() {

    }

}