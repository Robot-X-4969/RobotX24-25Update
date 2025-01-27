package robotx.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import robotx.modules.autonomous.AutonMethods;

@Autonomous(name = "ParkandPlace", group = "ParkandPlace")

public class ParkandPlace extends LinearOpMode {
    AutonMethods autonMethods;

    @Override

    public void runOpMode() {

        //Text at bottom of phone
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        autonMethods = new AutonMethods(this);
        autonMethods.init();

        final double tileTime = AutonMethods.tileTime;
        final double liftTime = AutonMethods.liftTime;

        autonMethods.start();

        waitForStart();

        if (opModeIsActive()) {
            autonMethods.driveForward(1);
            sleep(autonMethods.sleepTime(tileTime*2));
            autonMethods.stop();
            autonMethods.lift(0.5);
            sleep(autonMethods.sleepTime(liftTime));
            autonMethods.stopLift();
        }
    }


}