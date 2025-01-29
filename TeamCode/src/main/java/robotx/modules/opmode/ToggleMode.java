package robotx.modules.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.stx_libraries.XModule;

public class ToggleMode extends XModule {

    public ToggleMode(OpMode op) {
        super(op);
    }

    public boolean toggle = true;
    public void loop() {
        opMode.telemetry.addData("Dual-Player Drive", toggle);
        // button presses, calls methods
        if (xGamepad1.back.wasPressed()) {
            toggle = !toggle;
            ClawSystem.toggle = toggle;
            LiftSystem.toggle = toggle;
        }
    }
}