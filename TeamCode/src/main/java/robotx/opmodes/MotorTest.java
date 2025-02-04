package robotx.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.modules.opmode.testerOps.MotorTester;
import robotx.stx_libraries.XOpMode;


// sample change


@TeleOp(name = "XMotorTest", group = "TesterOps")
public class MotorTest extends XOpMode {
    MotorTester motorTester;

    public void initModules() {
        super.initModules();

        motorTester = new MotorTester(this);
        activeModules.add(motorTester);
    }
}

/*
Controls
GamePad 1:

Gamepad 2:

*/