package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.magic.ShutdownManager;

@Autonomous(name = "Red Front", group = "Autonomous")
public class RedFront extends LinearOpMode {
    private BaseRobot baseRobot;

    @Override
    public void runOpMode() {
        baseRobot = new BaseRobot(hardwareMap, gamepad1, gamepad2, telemetry);
        MainAuto auto = new MainAuto(baseRobot, "red");
        ShutdownManager shutdownManager = new ShutdownManager(this, baseRobot, auto);

        waitForStart();
        shutdownManager.scheduleShutdownCheck();
        try {
            if (opModeIsActive()) {
                auto.run("red front");
            }
        } catch (RuntimeException e) {
            /* The ShutdownManager has thrown a RuntimeException because the opmode has been stopped from the driver hub.
             * No cleanup is needed, because it is handled in the shutdown manager.
             */
        }
    }
}




