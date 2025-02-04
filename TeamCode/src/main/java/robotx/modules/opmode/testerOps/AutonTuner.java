package robotx.modules.opmode.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import robotx.stx_libraries.Stopwatch;
import robotx.stx_libraries.XModule;
import robotx.modules.autonomous.AutonMethods;

public class AutonTuner extends XModule {
    public int tileTime = 100;
    public int unit;

    public final Stopwatch stopWatch = new Stopwatch();

    public final OpMode opMode;
    public final AutonMethods autonMethods;

    public AutonTuner(OpMode opMode) {
        super(opMode);
        this.opMode = opMode;
        autonMethods = new AutonMethods(opMode);
    }

    public void init(){
        autonMethods.init();
    }

    public void setUnit(int newUnit){
        unit = newUnit;
        if(unit < 1){
            unit = 1;
        } else if(unit > 1000){
            unit = 1000;
        }
    }

    public void loop(){
        //Handles unit and tileTime changes
        if(xGamepad1.dpad_left.wasPressed()){
            setUnit(unit/10);
        }
        if(xGamepad1.dpad_right.wasPressed()){
            setUnit(unit*10);
        }
        if(xGamepad1.dpad_down.wasPressed()){
            tileTime -= unit;
        }
        if(xGamepad1.dpad_up.wasPressed()){
            tileTime += unit;
        }

        if(xGamepad1.a.wasPressed()){
            stopWatch.startTimer(tileTime);
            autonMethods.driveForward(1);
        }

        if(stopWatch.timerDone()){
            autonMethods.stop();
            stopWatch.reset();
        }
    }
}