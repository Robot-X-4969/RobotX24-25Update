package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.stx_libraries.XOpMode;
import robotx.stx_libraries.tuning.ConfigTester;
import robotx.stx_libraries.tuning.EncoderTester;


// sample change


@TeleOp(name = "LiftTester", group = "Tests")
public class LiftTester extends XOpMode {
    EncoderTester encoderTester;

    public void initModules() {

        super.initModules();

        encoderTester = new EncoderTester(this, new String[]{
                "liftMotor1", "liftMotor2"
        });
        activeModules.add(encoderTester);
    }
}