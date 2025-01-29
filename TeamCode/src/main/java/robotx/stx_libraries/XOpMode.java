package robotx.stx_libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

/**
 * XOpMode Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement XModules into OpModes.
 * <p>
 * Created by Nicholas on 11/3/16.
 */
public abstract class XOpMode extends OpMode {
    /**
     * List of XModules which runs throughout OpMode.
     */
    public ArrayList<XModule> activeModules = new ArrayList<>();
    /**
     * Modules which run only in the initialization process (i.e. modules which do not call start(), loop(), or stop())
     */
    public ArrayList<XModule> inactiveModules = new ArrayList<>();

    /**
     * XGamepad object for the primary OpMode gamepad.
     */
    public final XGamepad xGamepad1 = new XGamepad();
    /**
     * XGamepad object for the secodnary OpMode gamepad.
     */
    public final XGamepad xGamepad2 = new XGamepad();

    /**
     * Scheduling object used throughout OpMode.
     */
    public final Scheduler scheduler = new Scheduler();

    /**
     * Attaches modules to OpMode.
     */
    public void initModules() {
    }

    /**
     * Method called on OpMode initialization.
     * <p>
     * By default, this initializes and attaches schedule and xGamepads to all modules.
     */
    @Override
    public void init() {
        initModules();
        xGamepad1.update(gamepad1);
        xGamepad2.update(gamepad2);
        for (XModule module : activeModules) {
            module.init();
            module.scheduler = scheduler;
            module.xGamepad1 = xGamepad1;
            module.xGamepad2 = xGamepad2;
        }
        for (XModule module : inactiveModules) {
            module.init();
            module.scheduler = scheduler;
            module.xGamepad1 = xGamepad1;
            module.xGamepad2 = xGamepad2;
        }
    }

    /**
     * Method called while awaiting OpMode start.
     * <p>
     * By default, this calls the init_loop methods of all modules and updates the XGamepad objects.
     */
    @Override
    public void init_loop() {
        scheduler.loop();

        xGamepad1.update();
        xGamepad2.update();

        for (XModule module : activeModules) {
            module.init_loop();
        }
        for (XModule module : inactiveModules) {
            module.init_loop();
        }
    }

    /**
     * Method called on OpMode start.
     * <p>
     * By default, this calls the start methods of all active modules.
     */
    @Override
    public void start() {
        for (XModule module : activeModules) {
            module.start();
        }
    }

    /**
     * Method called while OpMode running.
     * <p>
     * By default, this calls the loop methods of all active modules and updates the XGamepad objects.
     */
    @Override
    public void loop() {
        scheduler.loop();

        xGamepad1.update();
        xGamepad2.update();

        for (XModule module : activeModules) {
            module.loop();
        }
    }

    /**
     * Method called on OpMode stop.
     * <p>
     * By default, this calls the stop methods of all active modules.
     */
    @Override
    public void stop() {
        for (XModule module : activeModules) {
            module.stop();
        }
    }
}