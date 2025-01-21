package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import robotx.libraries.PressHandler;

/**
 * Created by William on 3/9/23.
 * Use to test the servo at any position.
 * Hold down the back bumpers to change the unit,
 * and press the up and down buttons on the D-Pad to increment/decrement.
 */

@TeleOp(name = "LiftEncodertester", group = "Tests")
public class LiftEncoderTesterOp extends OpMode {
    DcMotor testMotor;
    DcMotor testMotor2;
    //DcMotor testMotor3;
    //DcMotor testMotor4;
    int encoderposition;
    int encoderposition2;
    int scale;
    Boolean isStopRequested = false;
    PressHandler gamepad1_dpad_up;
    PressHandler gamepad1_dpad_down;
    PressHandler gamepad1_dpad_right;
    PressHandler gamepad1_dpad_left;


    public void init() {
        //testMotor4 = hardwareMap.dcMotor.get("leftLift1");
        testMotor2 = hardwareMap.dcMotor.get("leftLift");
        //testMotor3 = hardwareMap.dcMotor.get("rightLift1");
        testMotor = hardwareMap.dcMotor.get("rightLift");

        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        testMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //testMotor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //testMotor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        testMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        testMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //testMotor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //testMotor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //testMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        //testMotor4.setDirection(DcMotorSimple.Direction.REVERSE);

        testMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        testMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //testMotor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //testMotor4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        scale = 100;
        encoderposition = 0;

        gamepad1_dpad_up = new PressHandler();
        gamepad1_dpad_down = new PressHandler();
        gamepad1_dpad_right = new PressHandler();
        gamepad1_dpad_left = new PressHandler();
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        gamepad1_dpad_up.update(gamepad1.dpad_up);
        gamepad1_dpad_down.update(gamepad1.dpad_down);
        gamepad1_dpad_left.update(gamepad1.dpad_left);
        gamepad1_dpad_right.update(gamepad1.dpad_right);

        if (gamepad1_dpad_right.onPress()) {
            scale *= 10;
        }
        if (gamepad1_dpad_left.onPress()) {
            scale /= 10;
        }
        if (gamepad1_dpad_up.onPress()) {
            encoderposition += scale;
            encoderposition2 -= scale;

            testMotor.setTargetPosition(encoderposition + scale);
            //testMotor3.setTargetPosition(encoderposition + scale);

            testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //testMotor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            testMotor.setPower(1);
            //testMotor3.setPower(1);

            testMotor2.setPower(1);
            //testMotor4.setPower(.5);

            while (testMotor.isBusy()) {
                if (isStopRequested) break;
            }

            testMotor.setPower(0);
            testMotor2.setPower(0);
            //testMotor3.setPower(0);
            //testMotor4.setPower(0);

        }
        if (gamepad1_dpad_down.onPress()) {
            encoderposition -= scale;
            encoderposition2 += scale;

            testMotor.setTargetPosition(encoderposition - scale);
            //testMotor3.setTargetPosition(encoderposition - scale);

            testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //testMotor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            testMotor.setPower(-1);
            //testMotor3.setPower(-1);

            testMotor2.setPower(-1);


            while (testMotor.isBusy()) {
                if (isStopRequested) break;
            }

            testMotor.setPower(0);
            testMotor2.setPower(0);
            //testMotor3.setPower(0);
            //testMotor4.setPower(0);
        }
        //displays the encoder location
        telemetry.addData("Scale:", scale);
        telemetry.addData("Goal Position", encoderposition);
        telemetry.addData("Motor 1 Position", testMotor.getCurrentPosition());
        telemetry.addData("Motor 2 Position", testMotor2.getCurrentPosition());
        //telemetry.addData("Motor 3 Position", testMotor3.getCurrentPosition());
        //telemetry.addData("Motor 4 Position", testMotor4.getCurrentPosition());

    }

    @Override
    public void stop() {
        isStopRequested = true;
    }

}
