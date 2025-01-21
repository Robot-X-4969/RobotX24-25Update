package robotx.modules.opmode.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import robotx.libraries.XModule;

/*
    Used for testing dead zones of a servo, not testing positions like servo tester
 */
public class TestServo extends XModule {
    public Servo servo;

    public TestServo(OpMode op) {
        super(op);
    }

    public void init(){
        servo = opMode.hardwareMap.servo.get("Servo");
    }
    public void loop(){
        if(xGamepad1().x.wasPressed()){
            servo.setPosition(0);
        }
        if(xGamepad1().b.wasPressed()){
            servo.setPosition(1);
        }
    }
}
