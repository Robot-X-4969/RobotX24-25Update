package robotx.modules.opmode.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import robotx.libraries.XModule;

/*
    Used for testing dead zones of a servo, not testing positions like servo tester
 */
public class DriveTester extends XModule {
    public DcMotor[] motors = new DcMotor[4];

    public DriveTester(OpMode op) {
        super(op);
    }

    public void init(){
        for(int i = 0; i < motors.length; i++){
            motors[i] = opMode.hardwareMap.dcMotor.get("motor"+i);
        }
    }
    public void loop(){
        for(DcMotor motor : motors){
            motor.setPower(0);
        }
        if(xGamepad1().y.isDown()){
            motors[0].setPower(1);
        }
        if(xGamepad1().b.isDown()){
            motors[1].setPower(1);
        }
        if(xGamepad1().a.isDown()){
            motors[2].setPower(1);
        }
        if(xGamepad1().x.isDown()){
            motors[3].setPower(1);
        }
    }
}
