package robotx.stx_libraries;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.List;

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
