package robotx.opmodes.autonomous.CvAuto;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.List;

import robotx.modules.autonomous.MecanumDrive;
import robotx.modules.opmode.OrientationDrive;

@Disabled

@Autonomous(name = "CvAutoBrick", group = "CvAuto")
public class BrickDetection extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    OpenCvWebcam phoneCam;
    SkystoneDeterminationPipeline pipeline;
    /**
     * {@link #aprilTag} is the variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * {@link #visionPortal} is the variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;


    @Override
    public void runOpMode() {


        // Setup, initialize, import modules

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //openCV camera / pipeline setup
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new SkystoneDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        // init pixelPos
        String pixelPos = "None";

        boolean programSelected = false;

        String sideSelect = null;

        telemetry.clearAll();

        telemetry.addData("Program running: ", sideSelect);
        telemetry.update();

        waitForStart();

        //code that runs
        while (opModeIsActive()) {

            // current opencv relationships found as of (11/21/23)
            /*

            Blue: Cb < Cr (50 < 200)
            Red: Cb > Cr (215 > 100)
            Grey: Cb = Cr

            Will vary based on light conditions, but will hold to be true
            Gray tiles are used, hence a spike in Cr means blue, spike in Cb is red

            Analysis1 = Cb
            Analysis2 = Cr
             */

            //for red side (currently)

            // scanning will work for both sides, just need to change which way we are moving
            //scan middle
            switch (sideSelect) {
                case "RSR":
                case "RSL":
                    for (int i = 0; i < 500; i++) {
                        if (pipeline.getAnalysis1() > pipeline.getAnalysis2() + 75) {
                            pixelPos = "Middle";
                            break;
                        }
                        sleep(5);
                    }
                    break;
                case "BSR":
                case "BSL":
                    for (int i = 0; i < 500; i++) {
                        if (pipeline.getAnalysis2() > pipeline.getAnalysis1() + 75) {
                            pixelPos = "Middle";
                            break;
                        }
                        sleep(5);
                    }
                    break;
            }

            /**
             * Need to test the value to move from one line to the other and if need to change camera view
             */
            int timeBetweenLines = 150;

            //scan outer (away from center)
            switch (sideSelect) {
                case "RSR":
                case "RSL":
                    for (int i = 0; i < 500; i++) {
                        if (pipeline.getAnalysis1() > pipeline.getAnalysis2() + 15) {
                            pixelPos = "Right";
                            break;
                        }
                        sleep(1);
                    }
                    break;
                case "BSR":
                case "BSL":
                    for (int i = 0; i < 500; i++) {
                        if (pipeline.getAnalysis2() > pipeline.getAnalysis1() + 15) {
                            pixelPos = "Right";
                            break;
                        }
                        sleep(1);
                    }
                    break;
            }

            //then logically has to be the other option
            if (pixelPos.equals("None")) {
                pixelPos = "Left";
            }

            telemetry.addData("Position", pixelPos);
            telemetry.update();

            // close OpenCV camera
            phoneCam.stopStreaming();
            phoneCam.stopRecordingPipeline();
            phoneCam.closeCameraDevice();

            //movements place a pixel and get robot to the board

            /*
            code to set up getting to apriltags

            left = 1 or 4
            middle = 2 or 5
            right = 3 or 6

            ######## AprilTags Code is constant no matter what and should work no matter where the robot is on the board - change the "ConstantTimeMove1" var to edit how much it is moving
            it is iterative, so could set to a low value it would just jerk a lot
             */

            int PlacementEval = 0;
            int escapeThereIsAProbem = 0;

            switch (pixelPos) {

                case "Left":
                    PlacementEval = 1;
                    break;
                case "Middle":
                    PlacementEval = 2;
                    break;
                case "Right":
                    PlacementEval = 3;
                    break;
            }

            //clear telemetry for apriltags
            telemetry.clear();

            //start camera for apriltags
            initAprilTag();

            int constantTimeMove1 = 75;
            int DetectionEval = 0;

            //dependent on location in relation to the board

            while (true) {

                aprilTag.getDetections();
                List<AprilTagDetection> currentDetections = aprilTag.getDetections();

                //find and save current detection
                for (AprilTagDetection detection : currentDetections) {
                    //1-left, 3-right
                    //time is not final

                    if (detection.id == 1 || detection.id == 4) {
                        // add in conditions for each
                        DetectionEval = 1;

                        telemetry.addData("Current april Tags Detection", detection.id);
                        telemetry.update();
                        break;
                    }
                    if (detection.id == 2 || detection.id == 5) {
                        // add in conditions for each
                        DetectionEval = 2;

                        telemetry.addData("Current april Tags Detection", detection.id);
                        telemetry.update();
                        break;
                    }
                    if (detection.id == 3 || detection.id == 6) {
                        // add in conditions for each
                        DetectionEval = 3;

                        telemetry.addData("Current april Tags Detection", detection.id);
                        telemetry.update();
                        break;
                    }

                }

                //catch not scanning properly

                if (DetectionEval == 0) {
                    telemetry.addData("FAIL", "FAIL");
                    telemetry.update();

                    escapeThereIsAProbem++;

                    //temp sleep for testing
                    sleep(1000);
                    continue;

                }

                //more error handling
                if (escapeThereIsAProbem > 10) {
                    telemetry.addData("FAIL", "WE ESCAPED");
                    telemetry.update();
                    break;
                }

                // if correct, score and end (stop looking)

                // in case of rr it might be funky
                if (PlacementEval == DetectionEval) {
                    break;
                }

            }
            //at this point should have a pixel placed on the wall and parked

            //sleep entire auto
            sleep(30000);

        }
        // apriltags end vision - don't kill CPU
        visionPortal.close();
    }

    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)

                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));
        builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);

    }   // end method initAprilTag()

    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

    }   // end method telemetryAprilTag()

    public static class SkystoneDeterminationPipeline extends OpenCvPipeline {

        /*
         * Some color constants
         */

        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(130, 150);
        //right side of object is even with inside of camera

        static final int REGION_WIDTH = 20;
        static final int REGION_HEIGHT = 20;

        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat region1_Cr;

        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        Mat Cr = new Mat();

        int avg1;
        int avg2;

        int avgCb;
        int avgCr;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        void inputToCr(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cr, 2);
        }


        public void init(Mat firstFrame) {

            inputToCb(firstFrame);
            inputToCr(firstFrame);

            region1_Cr = Cr.submat(new Rect(region1_pointA, region1_pointB));
            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }

        @Override
        public Mat processFrame(Mat input) {
            inputToCb(input);
            inputToCr(input);

            avgCb = (int) Core.mean(region1_Cb).val[0];
            avgCr = (int) Core.mean(region1_Cr).val[0];

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines


            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }

        public int getAnalysis1() {
            avg1 = avgCb;
            return avgCb;
        }

        public int getAnalysis2() {
            avg2 = avgCr;
            return avgCr;
        }
    }

    // special note for John - sleeps are to give the servos time to move

}
