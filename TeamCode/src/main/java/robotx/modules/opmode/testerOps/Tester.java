package robotx.modules.opmode.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.libraries.XServo;
import robotx.libraries.XModule;

public class Tester extends XModule {

    private final XServo[] servos;

    public Tester(OpMode op) {
        super(op);
        servos = new XServo[]{
                new XServo(op, "test", new double[]{
                        1.0, 0.0
                })
        };
    }

    public void init() {
        for(XServo servo : servos){
            servo.init();
        }
    }

    public void loop() {
        if(xGamepad1().a.wasPressed()){
            servos[0].forward();
        }
        if(xGamepad1().b.isDown()){
            servos[0].increment(-0.05);
        }
    }
}