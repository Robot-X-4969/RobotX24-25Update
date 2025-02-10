package robotx.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import robotx.modules.autonomous.AutonMethods;

@Autonomous(name = "Park", group = "Park")

public class Park extends LinearOpMode {

    AutonMethods autonMethods;

    @Override

    public void runOpMode() {
        autonMethods = new AutonMethods(this);
        autonMethods.init();

        final int tileTime = AutonMethods.tileTime;

        autonMethods.start();

        waitForStart();

        if (opModeIsActive()) {
            autonMethods.strafeRight(1);
            sleep(autonMethods.sleepTime(10000));
            autonMethods.stop();
        }
    }
}