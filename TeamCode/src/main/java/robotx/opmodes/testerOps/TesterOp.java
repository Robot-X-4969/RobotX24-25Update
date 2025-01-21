package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.libraries.XOpMode;
import robotx.modules.opmode.testerOps.Tester;


// sample change


@TeleOp(name = "TesterOp", group = "Tests")
public class TesterOp extends XOpMode {
    Tester tester;

    {

    }

    public void initModules() {

        super.initModules();

        tester = new Tester(this);
        activeModules.add(tester);
    }

    public void init() {
        super.init();
    }
}