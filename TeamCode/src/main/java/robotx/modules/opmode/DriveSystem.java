package robotx.modules.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import robotx.stx_libraries.drive.OrientationDrive;

public class DriveSystem extends OrientationDrive {

    public DriveSystem(OpMode op) {super(op);}

    @Override
    public void init(){
        super.init();
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void control_loop(){
        refreshStick();
        if (xGamepad1.x.wasPressed()) {
            resetOrientation();
        }
        if (xGamepad1.dpad_down.wasPressed()) {
            power -= 0.25;
        }
        if (xGamepad1.dpad_up.wasPressed()) {
            power += 0.25;
        }
    }
}
