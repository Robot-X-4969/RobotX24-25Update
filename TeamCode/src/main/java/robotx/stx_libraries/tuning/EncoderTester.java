package robotx.stx_libraries.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.stx_libraries.XModule;
import robotx.stx_libraries.XMotor;

/**
 * Created by John Daniher on 2/6/2025.
 */
public class EncoderTester extends XModule {
    private final String[] motorPaths;

    public final XMotor[] motors;
    public int index = 0;

    public int unit = 10;

    public EncoderTester(OpMode op, String[] motorPaths) {
        super(op);
        this.motorPaths = motorPaths;
        motors = new XMotor[motorPaths.length];
        for(int i = 0; i < motorPaths.length; i++){
            motors[i] = new XMotor(op, motorPaths[i]);
        }
    }

    @Override
    public void init(){
        for(XMotor motor : motors){
            motor.init();
            loopMotors.add(motor);
        }
    }

    @Override
    public void loop(){
        opMode.telemetry.addData("Controls", "\nD-Pad Left: Unit / 10\nD-Pad Right: Unit * 10\nD-Pad Up: Position+\nD-Pad Down: Position-\nA: Cycle forwards through motors\nB: Cycle backwards through motors");
        opMode.telemetry.addData("Testing Motor: ", motorPaths[index]);
        opMode.telemetry.addData("Motor Position: ", motors[index].getPosition());
        super.loop();
    }

    @Override
    public void control_loop(){
        if(xGamepad1.left_bumper.wasPressed()){
            unit /= 10;
            if(unit < 1){
                unit = 1;
            }
        }
        if(xGamepad1.right_bumper.wasPressed()){
            unit *= 10;
            if(unit > 1000){
                unit = 1000;
            }
        }
        if(xGamepad1.dpad_down.wasPressed()){
            motors[index].increment(-unit);
        }
        if(xGamepad1.dpad_up.wasPressed()){
            motors[index].increment(unit);
        }
        if(xGamepad1.a.wasPressed()){
            index++;
            if(index >= motors.length){
                index = 0;
            }
        }
        if(xGamepad1.b.wasPressed()){
            index--;
            if(index < 0){
                index = motors.length-1;
            }
        }
    }
}
