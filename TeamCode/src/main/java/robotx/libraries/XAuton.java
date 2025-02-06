package robotx.libraries;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.List;

import robotx.stx_libraries.XModule;

public class XAuton extends LinearOpMode {
    public final List<XModule> modules = new ArrayList<>();

    public void initModules(){
    }

    public void initialize(){
        initModules();
        for(XModule module : modules){
            module.init();
        }
    }

    public void run(){
    }

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        if(opModeIsActive()){
            run();
        }
    }
}
