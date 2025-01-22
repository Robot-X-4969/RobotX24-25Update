package robotx.libraries;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Created by John Daniher 1/21/25
 * Some code by Owen Stuckman 2024
 */
public class XCamera {
    private final OpMode opMode;
    private final String webcamPath;

    private OpenCvCamera camera;

    //Constructor; opMode and camera name required
    public XCamera(OpMode opMode, String webcamPath) {
        this.opMode = opMode;
        this.webcamPath = webcamPath;
    }

    //Initializes the camera
    public void init() {
        //Get the device name of the webcam to reference
        final WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, webcamPath);

        //Define and initialize the camera
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
        //Open camera and begin streaming through the camera
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Called when the camera is successfully opened; starts streaming at highest possible resolution (1920x1080)
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                // Called when there is an error opening the camera
                opMode.telemetry.addData("Error", "Camera failed to open with error code: " + errorCode);
                opMode.telemetry.update();
            }
        });
    }

    public void changeStream(int width, int height, OpenCvCameraRotation rotation){
        camera.stopStreaming();

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Called when the camera is successfully opened; starts streaming at highest possible resolution (1920x1080)
                camera.startStreaming(width, height, rotation);
            }

            @Override
            public void onError(int errorCode) {
                // Called when there is an error opening the camera
                opMode.telemetry.addData("Error", "Camera failed to open with error code: " + errorCode);
                opMode.telemetry.update();
            }
        });
    }
}
