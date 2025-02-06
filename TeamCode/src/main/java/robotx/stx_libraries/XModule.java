package robotx.stx_libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

/**
 * XModule Class
 * <p>
 * Custom class by FTC Team 4969 RobotX for better access to opMode, XGamepad, and Scheduling instances.
 * <p>
 * Created by Nicholas on 11/3/16.
 */
public abstract class XModule {
    /**
     * OpMode's instance.
     */
    public final OpMode opMode;
    /**
     * OpMode's scheduling object.
     */
    public Scheduler scheduler;
    /**
     * XGamepad object for the primary OpMode gamepad.
     */
    public XGamepad xGamepad1 = new XGamepad();
    /**
     * XGamepad object for the secondary OpMode gamepad.
     */
    public XGamepad xGamepad2 = new XGamepad();

    /**
     * List of module's motors to be called every loop.
     */
    public final List<XMotor> loopMotors = new ArrayList<>();

    /**
     * XModule Constructor
     * <p>
     * Implements opMode, schedule, and xGamepad variables automatically into modules.
     *
     * @param op The OpMode's instance.
     */
    public XModule(OpMode op) {
        opMode = op;
    }

    /**
     * Update method which refreshes xGamepad objects.
     */
    public void update() {
        scheduler.loop();
        xGamepad1.update(opMode.gamepad1);
        xGamepad2.update(opMode.gamepad2);
    }

    /**
     * Method called on OpMode initialization.
     */
    public void init() {
        for(XMotor motor : loopMotors){
            motor.init();
        }
    }

    /**
     * Method called while awaiting OpMode start.
     */
    public void init_loop() {
        for (XMotor motor : loopMotors) {
            motor.loop();
        }
        opMode.telemetry.update();
    }

    /**
     * Method called on OpMode start.
     */
    public void start() {
    }

    /**
     * Method which handles inputs.
     */
    public void control_loop() {
    }

    /**
     * Method called while OpMode running.
     * <p>
     * By default, this loops all loopMotors.
     */
    public void loop() {
        control_loop();
        opMode.telemetry.update();
        for (XMotor motor : loopMotors) {
            motor.loop();
        }
    }


    /**
     * Method called on OpMode stop.
     */
    public void stop() {
    }
}
