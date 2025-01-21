package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.libraries.XOpMode;


// sample change


@TeleOp(name = "DriveTester", group = "Tests")
public class DriveTester extends XOpMode {
    robotx.modules.opmode.testerOps.DriveTester driveTester;

    {

    }

    public void initModules() {

        super.initModules();

        driveTester = new robotx.modules.opmode.testerOps.DriveTester(this);
        activeModules.add(driveTester);
    }

    public void init() {
        super.init();
    }
}