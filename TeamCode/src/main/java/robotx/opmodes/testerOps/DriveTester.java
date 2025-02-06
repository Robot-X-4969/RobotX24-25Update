package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.stx_libraries.XOpMode;
import robotx.stx_libraries.tuning.ConfigTester;


// sample change


@TeleOp(name = "DriveTester", group = "Tests")
public class DriveTester extends XOpMode {
    ConfigTester configTester;

    public void initModules() {

        super.initModules();

        configTester = new ConfigTester(this, 4, 0);
        activeModules.add(configTester);
    }

    public void init() {
        super.init();
    }
}