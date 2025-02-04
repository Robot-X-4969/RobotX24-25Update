package robotx.stx_libraries;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * XGamepad Class
 * <p>
 * Custom class by FTC Team 4969 RobotX to better implement gamepad functionality.
 * <p>
 * Created by Nicholas on 11/21/16.
 */
public class XGamepad {
    // Joystick position floats
    public float left_stick_x;
    public float left_stick_y;
    public float right_stick_x;
    public float right_stick_y;

    // Trigger pressure floats
    public float left_trigger;
    public float right_trigger;

    // Buttons; Uses custom Button class
    public final Button dpad_up = new Button();
    public final Button dpad_down = new Button();
    public final Button dpad_left = new Button();
    public final Button dpad_right = new Button();
    public final Button a = new Button();
    public final Button b = new Button();
    public final Button x = new Button();
    public final Button y = new Button();
    public final Button guide = new Button();
    public final Button start = new Button();
    public final Button back = new Button();
    public final Button left_bumper = new Button();
    public final Button right_bumper = new Button();
    public final Button left_stick_button = new Button();
    public final Button right_stick_button = new Button();

    private Gamepad gamepad;

    /**
     * Custom object class which incorporates custom Button class into gamepad functionality.
     */
    public XGamepad() {
    }

    /**
     * Custom object class which incorporates custom Button class into gamepad functionality.
     */
    public XGamepad(Gamepad gamepad) {
        update(gamepad);
    }

    /**
     * Updates the button variables based on a provided gamepad object and sets gamepad variable.
     *
     * @param gamepad The gamepad data to update the XGamepad with.
     */
    public void update(Gamepad gamepad) {
        this.gamepad = gamepad;
        //Updates floats
        left_stick_x = gamepad.left_stick_x;
        left_stick_y = gamepad.left_stick_y;
        right_stick_x = gamepad.right_stick_x;
        right_stick_y = gamepad.right_stick_y;
        left_trigger = gamepad.left_trigger;
        right_trigger = gamepad.right_trigger;

        //Updates buttons
        dpad_up.update(gamepad.dpad_up);
        dpad_down.update(gamepad.dpad_down);
        dpad_left.update(gamepad.dpad_left);
        dpad_right.update(gamepad.dpad_right);
        a.update(gamepad.a);
        b.update(gamepad.b);
        x.update(gamepad.x);
        y.update(gamepad.y);
        guide.update(gamepad.guide);
        start.update(gamepad.start);
        back.update(gamepad.back);
        left_bumper.update(gamepad.left_bumper);
        right_bumper.update(gamepad.right_bumper);
        left_stick_button.update(gamepad.left_stick_button);
        right_stick_button.update(gamepad.right_stick_button);
    }

    /**
     * Updates the button variables based on any preset gamepad.
     */
    public void update() {
        //Updates floats
        left_stick_x = gamepad.left_stick_x;
        left_stick_y = gamepad.left_stick_y;
        right_stick_x = gamepad.right_stick_x;
        right_stick_y = gamepad.right_stick_y;
        left_trigger = gamepad.left_trigger;
        right_trigger = gamepad.right_trigger;

        //Updates buttons
        dpad_up.update(gamepad.dpad_up);
        dpad_down.update(gamepad.dpad_down);
        dpad_left.update(gamepad.dpad_left);
        dpad_right.update(gamepad.dpad_right);
        a.update(gamepad.a);
        b.update(gamepad.b);
        x.update(gamepad.x);
        y.update(gamepad.y);
        guide.update(gamepad.guide);
        start.update(gamepad.start);
        back.update(gamepad.back);
        left_bumper.update(gamepad.left_bumper);
        right_bumper.update(gamepad.right_bumper);
        left_stick_button.update(gamepad.left_stick_button);
        right_stick_button.update(gamepad.right_stick_button);
    }
}