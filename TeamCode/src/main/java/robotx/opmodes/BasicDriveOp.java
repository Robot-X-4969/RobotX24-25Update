package robotx.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.libraries.XOpMode;
import robotx.modules.opmode.OrientationDrive;

@TeleOp(name = "Basic Drive", group = "CurrentOp")
public class BasicDriveOp extends XOpMode {
    OrientationDrive orientationDrive;

    {

    }

    public void initModules() {

        super.initModules();

        orientationDrive = new OrientationDrive(this);
        activeModules.add(orientationDrive);

    }

    public void init() {
        super.init();
    }
}