package robotx.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import robotx.stx_libraries.XOpMode;
import robotx.stx_libraries.drive.MecanumOrientationDrive;

@TeleOp(name = "Basic Drive", group = "CurrentOp")
public class BasicDriveOp extends XOpMode {
    MecanumOrientationDrive mecanumOrientationDrive;

    {

    }

    public void initModules() {

        super.initModules();

        mecanumOrientationDrive = new MecanumOrientationDrive(this);
        activeModules.add(mecanumOrientationDrive);

    }

    public void init() {
        super.init();
    }
}