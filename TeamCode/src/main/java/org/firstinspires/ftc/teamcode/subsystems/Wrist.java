package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.ServoEx;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.apache.commons.math3.analysis.function.Max;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Wrist extends SubsystemBase {

    public ServoEx servo;
    private Telemetry tm;
    public boolean Active = false;
    public int CurrentAngle;
    public int MinimumAngle = -50;
    public int MaximumAngle = 50;


    public Wrist(HardwareMap hardwareMap, Telemetry telemetry){
        tm = telemetry;
        //TODO: fix this name from config
        servo = new SimpleServo(hardwareMap, "wrist", MinimumAngle, MaximumAngle);
        servo.setInverted(false);
        //setAngle(0);
    }

    public void addFifteen() {
        setAngle(0);
    }
    public void subFifteen() {
        setAngle(40);
    }

    public void Goto(int angle) {
        CurrentAngle = angle;
        setAngle(angle);
    }
    public void AddDegree() {
        if (Active && CurrentAngle < MaximumAngle) {
            CurrentAngle++;
            setAngle(CurrentAngle);
        }
    }

    public void RemoveDegree() {
        if (Active && CurrentAngle > MinimumAngle) {
            CurrentAngle--;
            setAngle(CurrentAngle);
        }
    }

    public void setAngle(double angle){
        tm.addData("wrist set angle", angle);
        if (this.Active == true) {
            servo.turnToAngle(angle);
        }
    }
    public void open(){
        setAngle(200);
    }
    public void close(){
        setAngle(-25);
    }

    @Override
    public void periodic(){
        tm.addData("wrist active", this.Active);
        tm.addData("wrist angle", servo.getAngle());
    }
}