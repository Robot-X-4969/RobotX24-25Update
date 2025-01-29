package robotx.modules.opmode.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import robotx.stx_libraries.XModule;
import robotx.stx_libraries.XMotor;

/*
    Used for testing dead zones of a servo, not testing positions like servo tester
 */
public class MotorTester extends XModule {
    public XMotor motor;

    public MotorTester(OpMode op) {
        super(op);
        motor = new XMotor(op, "motor", 537);
    }

    public void init(){
        motor.init();
        motor.setPower(1);
        loopMotors.add(motor);
    }

    public void loop(){
        super.loop();
        if(xGamepad1.a.wasPressed()){
            motor.setIndefiniteRotation();
            motor.setRPM(60);
        }
        if(xGamepad1.b.wasPressed()){
            scheduler.schedule(1000, () -> {
                motor.setIndefiniteRotation(-1);
            });
        }
    }
}
