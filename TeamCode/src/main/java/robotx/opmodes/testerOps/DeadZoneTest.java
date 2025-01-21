package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.libraries.XOpMode;
import robotx.modules.opmode.testerOps.TestServo;

@TeleOp(name = "DeadZone Tester", group = "Tests")
public class DeadZoneTest extends XOpMode {
    TestServo testServo;

    public void initModules() {

        super.initModules();

        testServo = new TestServo(this);
        activeModules.add(testServo);
    }

    public void init() {
        super.init();
    }
}