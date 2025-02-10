package robotx.modules.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.stx_libraries.XModule;
import robotx.stx_libraries.XServo;

public class ClawSystem extends XModule {
    public static final double increment = 0.01 * 3 / 20;

    private final XServo clawServo, rotationServo;
    private final XServo[] mountServos;

    public ClawSystem(OpMode op) {
        super(op);
        clawServo = new XServo(op, "clawServo", new double[]{
                0.5, 0.75
        });
        rotationServo = new XServo(op, "rotationServo", 0.5);
        mountServos = new XServo[]{
                new XServo(op, "mountServo1", new double[]{
                        1, .35
                }),
                new XServo(op, "mountServo2", new double[]{
                        0, .65 * 3 / 20
                })
        };
    }

    @Override
    public void init() {
        clawServo.init();
        rotationServo.init();
        for (XServo servo : mountServos) {
            servo.init();
        }
    }

    public void claw() {
        clawServo.forward();
    }

    public void resetRotation(){
        rotationServo.forward();
    }

    public void rotateMount(boolean forward){
        if(forward){
            for (XServo servo : mountServos) {
                servo.forward();
            }
        } else {
            for (XServo servo : mountServos) {
                servo.backward();
            }
        }
    }

    @Override
    public void control_loop() {
        //2nd Driver Controls
        if (dualPlayer) {
            if (xGamepad2.a.wasPressed()) {
                resetRotation();
                rotateMount(true);
            }
            if (xGamepad2.x.wasPressed()) {
                resetRotation();
                rotateMount(false);
            }
            if (xGamepad2.b.wasPressed()) {
                claw();
            }
            if (xGamepad2.dpad_left.isDown()) {
                rotationServo.increment(-increment);
            }
            if (xGamepad2.dpad_right.isDown()) {
                rotationServo.increment(increment);
            }
        }

        //1st Driver Controls; Overrides
        if (xGamepad1.a.wasPressed()) {
            resetRotation();
            rotateMount(true);
        }
        if (xGamepad1.b.wasPressed()) {
            claw();
        }
        if (xGamepad1.dpad_left.isDown()) {
            rotationServo.increment(-increment);
        }
        if (xGamepad1.dpad_right.isDown()) {
            rotationServo.increment(increment);
        }
    }
}