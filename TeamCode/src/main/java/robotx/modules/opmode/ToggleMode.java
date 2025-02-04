package robotx.modules.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.stx_libraries.XModule;

public class ToggleMode extends XModule {

    public ToggleMode(OpMode op) {
        super(op);
    }

    public boolean toggle = true;

    @Override
    public void loop() {
        super.loop();
        opMode.telemetry.addData("Dual-Player Drive", toggle);
    }

    @Override
    public void control_loop(){
        // button presses, calls methods
        if (xGamepad1.back.wasPressed()) {
            toggle = !toggle;
            ClawSystem.toggle = toggle;
            LiftSystem.toggle = toggle;
        }
    }
}