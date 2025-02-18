package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.stx_libraries.XOpMode;
import robotx.stx_libraries.tuning.ConfigTester;


// sample change


@TeleOp(name = "ClawTester", group = "Tests")
public class ClawTester extends XOpMode {
    ConfigTester configTester;

    public void initModules() {

        super.initModules();

        configTester = new ConfigTester(this, 0, 4);
        activeModules.add(configTester);
    }

    public void init() {
        super.init();
    }
}