package robotx.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.modules.opmode.DriveSystem;
import robotx.stx_libraries.XOpMode;
import robotx.modules.opmode.ClawSystem;
import robotx.modules.opmode.LiftSystem;
import robotx.modules.opmode.ToggleMode;


// sample change


@TeleOp(name = "OpMode 24-25", group = "CurrentOp")
public class OpMode2025 extends XOpMode {
    DriveSystem driveSystem;
    LiftSystem liftSystem;
    ClawSystem clawSystem;
    ToggleMode toggleMode;

    public void initModules() {
        driveSystem = new DriveSystem(this);
        activeModules.add(driveSystem);

        liftSystem = new LiftSystem(this);
        activeModules.add(liftSystem);

        clawSystem = new ClawSystem(this);
        activeModules.add(clawSystem);

        toggleMode = new ToggleMode(this);
        activeModules.add(toggleMode);
    }
}

/*
Controls
GamePad 1:

Gamepad 2:

*/