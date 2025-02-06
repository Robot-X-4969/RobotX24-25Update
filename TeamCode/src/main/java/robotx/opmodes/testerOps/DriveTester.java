package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.stx_libraries.XOpMode;
import robotx.stx_libraries.tuning.MotorConfigTester;


// sample change


@TeleOp(name = "DriveTester", group = "Tests")
public class DriveTester extends XOpMode {
    MotorConfigTester motorConfigTester;

    public void initModules() {

        super.initModules();

        motorConfigTester = new MotorConfigTester(this, 4);
        activeModules.add(motorConfigTester);
    }

    public void init() {
        super.init();
    }
}