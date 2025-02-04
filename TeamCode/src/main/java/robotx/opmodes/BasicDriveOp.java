package robotx.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.stx_libraries.XOpMode;
import robotx.stx_libraries.drive.OrientationDrive;

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