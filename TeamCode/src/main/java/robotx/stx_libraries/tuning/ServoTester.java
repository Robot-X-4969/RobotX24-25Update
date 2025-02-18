package robotx.stx_libraries.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.ArrayList;
import java.util.List;

import robotx.stx_libraries.XModule;
import robotx.stx_libraries.XServo;

/**
 * Created by John Daniher on 2/6/2025.
 */
public class ServoTester extends XModule {
    private final String[] servoPaths;

    public final XServo[] servos;
    public int index = 0;

    public double unit = 0.01;
    public final boolean[] enabled;

    public ServoTester(OpMode op, String[] servoPaths) {
        super(op);
        this.servoPaths = servoPaths;
        servos = new XServo[servoPaths.length];
        enabled = new boolean[servoPaths.length];
        for(int i = 0; i < servoPaths.length; i++){
            servos[i] = new XServo(op, servoPaths[i], 0.5);
        }
    }

    public ServoTester(OpMode op, String[] servoPaths, double[] positions) {
        super(op);
        this.servoPaths = servoPaths;
        servos = new XServo[servoPaths.length];
        enabled = new boolean[servoPaths.length];
        for(int i = 0; i < servoPaths.length; i++){
            servos[i] = new XServo(op, servoPaths[i], positions[i]);
        }
    }

    @Override
    public void init(){
        for(XServo servo : servos){
            servo.init();
        }
    }

    @Override
    public void loop(){
        for(int i = 0; i < servos.length; i++){
            servos[i].setEnabled(i == index || enabled[i]);
        }
        opMode.telemetry.addData("Controls", "\nD-Pad Left: Unit / 10\nD-Pad Right: Unit * 10\nD-Pad Up: Position+\nD-Pad Down: Position-\nA: Cycle forwards through servos\nB: Cycle backwards through servos\nX: Toggle PWM");
        opMode.telemetry.addData("Testing Servo: ", servoPaths[index]);
        opMode.telemetry.addData("Servo Position: ", servos[index].getPosition());
        super.loop();
    }

    @Override
    public void control_loop(){
        if(xGamepad1.left_bumper.wasPressed()){
            unit /= 10;
            if(unit < 0.001){
                unit = 0.001;
            }
        }
        if(xGamepad1.right_bumper.wasPressed()){
            unit *= 10;
            if(unit > 0.1){
                unit = 0.1;
            }
        }
        if(xGamepad1.dpad_down.wasPressed()){
            servos[index].increment(-unit);
        }
        if(xGamepad1.dpad_up.wasPressed()){
            servos[index].increment(unit);
        }
        if(xGamepad1.a.wasPressed()){
            index++;
            if(index >= servos.length){
                index = 0;
            }
        }
        if(xGamepad1.b.wasPressed()){
            index--;
            if(index < 0){
                index = servos.length-1;
            }
        }
        if(xGamepad1.x.wasPressed()){
            enabled[index] = !enabled[index];
        }
    }
}
