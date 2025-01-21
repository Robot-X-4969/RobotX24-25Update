package robotx.modules.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import robotx.libraries.XModule;

public class OdomSystem extends XModule {

    //var setup

    public DigitalChannel touchSensor;

    boolean blocked = false;

    //methods are built into one button as a toggle


    public OdomSystem (OpMode op) {
        super(op);
    }

    public void init() {
        // pulls servos from configs
        //odom1 = opMode.hardwareMap.servo.get("frontOdom");
        //odom2 = opMode.hardwareMap.servo.get("leftOdom");
        //odom3 = opMode.hardwareMap.servo.get("rightOdom");
    }

    public void loop() {
        // button presses, calls methods

    }
}

// dpad down - odom wheel up/down on a toggle
// - gamepad 1
