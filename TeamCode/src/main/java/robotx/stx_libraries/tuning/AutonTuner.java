package robotx.stx_libraries.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.stx_libraries.drive.MecanumDrive;

public class AutonTuner extends MecanumDrive {
    /**
     * The time, in milliseconds, being tested for the robot to drive the length of 1 tile.
     */
    public int tileTime = 300;
    /**
     * The unit at which the tileTime scales by on change.
     */
    public int unit = 10;

    public AutonTuner(OpMode op) {
        super(op);
    }

    /**
     * Method which runs while opMode is running.
     * <p>
     * Updates telemetry to display values.
     */
    @Override
    public void loop() {
        opMode.telemetry.addData("Controls", "\nA: Drive\nD-Pad Left: Unit / 10\nD-Pad Right: Unit * 10\nD-Pad Up: TileTime+\nD-Pad Down: TileTime-\n");
        opMode.telemetry.addData("TileTime: ", tileTime + " ms");
        opMode.telemetry.addData("Unit: ", unit + " ms");

        super.loop();
    }

    /**
     * Method which runs to check controller inputs.
     * <p>
     * Checks dpad for value changes, checks A button for run command.
     */
    @Override
    public void control_loop() {
        // Checks controls for tileTime changes.
        if (xGamepad1.dpad_up.wasPressed()) {
            tileTime += unit;
        }
        if (xGamepad1.dpad_down.wasPressed()) {
            tileTime -= unit;
            if (tileTime < 0) {
                tileTime = 1;
            }
        }

        // Checks controls for unit changes.
        if (xGamepad1.dpad_right.wasPressed()) {
            unit *= 10;
            if (unit > 1000) {
                unit = 1000;
            }
        }
        if (xGamepad1.dpad_left.wasPressed()) {
            unit /= 10;
            if (unit < 1) {
                unit = 1;
            }
        }

        // Check controls for run start.
        if (xGamepad1.a.wasPressed()) {
            drive(1);
            // Schedules stop in tileTime milliseconds
            scheduler.schedule(tileTime, this::stopMotors);
        }
    }
}
