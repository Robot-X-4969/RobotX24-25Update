package robotx.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.libraries.XOpMode;
import robotx.modules.opmode.ClawSystem;
import robotx.modules.opmode.LiftSystem;
import robotx.modules.opmode.OrientationDrive;
import robotx.modules.opmode.ToggleMode;


// sample change


@TeleOp(name = "OpMode 24-25", group = "CurrentOp")
public class OpMode2025 extends XOpMode {
    OrientationDrive orientationDrive;
    LiftSystem liftSystem;
    ClawSystem clawSystem;
    ToggleMode toggleMode;

    {

    }

    public void initModules() {

        super.initModules();

        orientationDrive = new OrientationDrive(this);
        activeModules.add(orientationDrive);

        liftSystem = new LiftSystem(this);
        activeModules.add(liftSystem);

        clawSystem = new ClawSystem(this);
        activeModules.add(clawSystem);

        toggleMode = new ToggleMode(this);
        activeModules.add(toggleMode);
    }

    public void init() {
        super.init();
    }
    /*
    @Override
    public void start() {

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }
    */

}

/*
Controls
GamePad 1:

Gamepad 2:

*/