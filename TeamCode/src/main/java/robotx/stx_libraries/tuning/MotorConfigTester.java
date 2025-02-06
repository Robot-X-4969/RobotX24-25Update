package robotx.stx_libraries.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;

import robotx.stx_libraries.XModule;

/**
 * DriveTester Class
 * <p>
 * Custom class by FTC Team 4969 RobotX for testing drive train motors while configuring robot wiring.
 * <p>
 * Created by John Daniher on 2/5/2025.
 */
public class MotorConfigTester extends XModule {
    /**
     * The array of motors to test.
     */
    public final DcMotor[] motors;

    /**
     * Motors currently powered.
     */
    public final List<String> poweredMotors = new ArrayList<>();
    /**
     * The point in the motors array which is currently being tested.
     */
    public int motorIndex = 0;

    /**
     * Basic DriveTester Module.
     *
     * @param op The OpMode the DriveTester Module is created in.
     */
    public MotorConfigTester(OpMode op, int motorCount) {
        super(op);
        motors = new DcMotor[motorCount];
    }

    /**
     * Method which runs on bot initialization.
     * <p>
     * Initializes all motors; configured under ID "motor{0-3}"
     */
    @Override
    public void init() {
        for (int i = 0; i < motors.length; i++) {
            motors[i] = opMode.hardwareMap.dcMotor.get("motor" + i);
        }
    }

    /**
     * Method which runs while opMode running.
     * <p>
     * Checks controller inputs and stops all motors not set.
     */
    @Override
    public void loop() {
        for (DcMotor motor : motors) {
            motor.setPower(0);
        }
        opMode.telemetry.addData("Controls:", "\nD-Pad: Cycle Motors"
                + "\nY: motor" + (motorIndex % motors.length)
                + "\nB: motor" + ((motorIndex + 1) % motors.length)
                + "\nA: motor" + ((motorIndex + 2) % motors.length)
                + "\nX: motor" + ((motorIndex + 3) % motors.length) + "\n");
        opMode.telemetry.addData("Powered Motors:", poweredMotors);
        poweredMotors.clear();
        super.loop();
    }

    /**
     * Method which checks controller inputs.
     * <p>
     * Checks dpad for motor cycling, and checks letter buttons for motor powering.
     */
    @Override
    public void control_loop() {
        if (xGamepad1.dpad_down.wasPressed() || xGamepad1.dpad_left.wasPressed()) {
            motorIndex--;
            if (motorIndex < 0) {
                motorIndex = motors.length;
            }
        }
        if (xGamepad1.dpad_up.wasPressed() || xGamepad1.dpad_right.wasPressed()) {
            motorIndex++;
        }

        // Powers motors based off of button input, incrementing with index
        if (xGamepad1.y.isDown()) {
            motors[motorIndex % motors.length].setPower(1);
            poweredMotors.add("motor" + (motorIndex % motors.length));
        }
        if (xGamepad1.b.isDown()) {
            motors[(motorIndex + 1) % motors.length].setPower(1);
            poweredMotors.add("motor" + ((motorIndex + 1) % motors.length));
        }
        if (xGamepad1.a.isDown()) {
            motors[(motorIndex + 2) % motors.length].setPower(1);
            poweredMotors.add("motor" + ((motorIndex + 2) % motors.length));
        }
        if (xGamepad1.x.isDown()) {
            motors[(motorIndex + 3) % motors.length].setPower(1);
            poweredMotors.add("motor" + ((motorIndex + 3) % motors.length));
        }
    }
}
