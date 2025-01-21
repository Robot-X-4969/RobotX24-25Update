package robotx.opmodes.testerOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
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

import robotx.modules.autonomous.MecanumDrive;
import robotx.modules.autonomous.OdomSystem;
import robotx.modules.opmode.OrientationDrive;

@TeleOp(name = "OpenCvScan", group = "Default")
public class OpenCvScan extends LinearOpMode {

    OpenCvWebcam phoneCam;
    SkystoneDeterminationPipeline pipeline;
    MecanumDrive mecanumDrive;
    OrientationDrive orientationDrive;
    OdomSystem odomSystem;

    @Override
    public void runOpMode() {

        // Setup, initialize, import modules

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        orientationDrive = new OrientationDrive(this);
        orientationDrive.init();

        mecanumDrive = new MecanumDrive(this);
        mecanumDrive.init();

        odomSystem = new OdomSystem(this);
        odomSystem.init();

        mecanumDrive.start();
        orientationDrive.start();

        mecanumDrive.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mecanumDrive.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mecanumDrive.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mecanumDrive.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        double placementVar = 0;
        int x = 0;
        int SleepTime = 1500;
        int sleepTime = 1000;
        String coneColor = null;

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
                phoneCam.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        waitForStart();

        while (opModeIsActive()) {
            // Don't burn CPU cycles busy-looping in this sample

            telemetry.addData("AnalysisCb", pipeline.getAnalysis1());
            telemetry.addData("AnalysisCr", pipeline.getAnalysis2());
            telemetry.update();

            sleep(50);

        }
    }


    public static class SkystoneDeterminationPipeline extends OpenCvPipeline {

        /*
         * Some color constants
         */

        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        static final int duckThreshold = 100;

        /*
         * The core values which define the location and size of the sample regions
         */
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(73, 170);
        //right side of object is even with inside of camera

        static final int REGION_WIDTH = 20;
        static final int REGION_HEIGHT = 20;
        //200, 150 right
        static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(205, 175);
        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
        Point region2_pointA = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x,
                REGION2_TOPLEFT_ANCHOR_POINT.y);
        Point region2_pointB = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat region1_Cr;
        Mat region1_3;
        Mat region1_0;

        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        Mat Cr = new Mat();
        Mat Test3 = new Mat();
        Mat Test0 = new Mat();

        int avg1;
        int avg2;

        int avgCb;
        int avgCr;
        int avg3;
        int avg0;

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

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines


            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
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


}