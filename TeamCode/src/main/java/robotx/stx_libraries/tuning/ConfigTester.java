package robotx.stx_libraries.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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
public class ConfigTester extends XModule {
    /**
     * The array of motors to test.
     */
    public final DcMotor[] motors;

    /**
     * The array of servos to test.
     */
    public final Servo[] servos;

    /**
     * Motors currently powered.
     */
    public final List<String> poweredMotors = new ArrayList<>();
    /**
     * Motors currently powered.
     */
    public final List<String> activeServos = new ArrayList<>();
    
    public boolean servoMode = false;

    /**
     * The point in the motors array which is currently being tested.
     */
    public int index = 0;

    /**
     * Basic DriveTester Module.
     *
     * @param op The OpMode the DriveTester Module is created in.
     */
    public ConfigTester(OpMode op, int motorCount, int servoCount) {
        super(op);
        motors = new DcMotor[motorCount];
        servos = new Servo[servoCount];
        if(motors.length == 0){
            servoMode = true;
        }
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
        for (int i = 0; i < servos.length; i++){
            servos[i] = opMode.hardwareMap.servo.get("servo"+i);
            servos[i].setPosition(0.5);
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
        if(motors.length > 0){
            opMode.telemetry.addData("Controls:", "\nD-Pad: Cycle Motors"
                    + "\nY: motor" + (index % motors.length)
                    + "\nB: motor" + ((index + 1) % motors.length)
                    + "\nA: motor" + ((index + 2) % motors.length)
                    + "\nX: motor" + ((index + 3) % motors.length) + "\n");
        }
        opMode.telemetry.addData("Powered Motors: ", poweredMotors);
        opMode.telemetry.addData("Open Servos: ", activeServos);
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
            index--;
            if (index < 0) {
                index = motors.length;
            }
        }
        if (xGamepad1.dpad_up.wasPressed() || xGamepad1.dpad_right.wasPressed()) {
            index++;
        }
        if(xGamepad1.left_bumper.wasPressed() || xGamepad1.right_bumper.wasPressed()){
            if(servos.length != 0 && motors.length != 0){
                servoMode = !servoMode;
            }
        }

        // Powers motors based off of button input, incrementing with index
        if (xGamepad1.y.isDown()) {
            if(servoMode){
                toggleServo(0);
            } else {
                powerMotor(0);                
            }
        }
        if (xGamepad1.b.isDown()) {
            if(servoMode){
                toggleServo(1);
            } else {
                powerMotor(1);
            }
        }
        if (xGamepad1.a.isDown()) {
            if(servoMode){
                toggleServo(2);
            } else {
                powerMotor(2);
            }
        }
        if (xGamepad1.x.isDown()) {
            if(servoMode){
                toggleServo(3);
            } else {
                powerMotor(3);
            }
        }
    }
    
    public void powerMotor(int motorIndex){
        motors[(index + motorIndex) % motors.length].setPower(1);
        poweredMotors.add("motor" + ((index + motorIndex) % motors.length));
    }

    public void toggleServo(int servoIndex){
        int servosIndex = (index + servoIndex) % servos.length;
        Servo servo = servos[servosIndex];
        if(servo.getPosition() == 0.5){
            servo.setPosition(0.6);
            activeServos.add("servo"+servosIndex);
        } else {
            servo.setPosition(0.5);
            activeServos.remove("servo"+servosIndex);
        }
    }
}
