package robotx.modules.opmode.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import robotx.stx_libraries.tuning.AutonTuner;

public class AutonTuner2025 extends AutonTuner {
    public AutonTuner2025(OpMode op) {
        super(op);
    }

    @Override
    public void init(){
        super.init();
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
