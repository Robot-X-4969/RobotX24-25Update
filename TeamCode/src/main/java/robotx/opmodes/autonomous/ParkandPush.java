package robotx.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import robotx.modules.autonomous.AutonMethods;

@Autonomous(name = "ParkandPush", group = "Park")

public class ParkandPush extends LinearOpMode {
    AutonMethods autonMethods;

    @Override

    public void runOpMode() {

        //Text at bottom of phone
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        autonMethods = new AutonMethods(this);
        autonMethods.init();

        final double tileTime = AutonMethods.tileTime;

        autonMethods.start();

        waitForStart();

        if (opModeIsActive()) {
            autonMethods.strafeRight(1);
            sleep(autonMethods.sleepTime(tileTime*4));
            autonMethods.stop();
        }
    }


}