package robotx.modules.autonomous;

import android.location.Location;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;

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


public class OpenCV extends OpenCvPipeline {
    Telemetry telemetry;

    String position = "no scan";

    public OpenCV(Telemetry t) { telemetry = t; }
    /*
     * Some color constants
     */

    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    /*
     * The core values which define the location and size of the sample regions
     */
    // 70, 130 middle
    static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(73, 170);
    //right side of object is even with inside of camera

    static final int REGION1_WIDTH = 15;
    static final int REGION1_HEIGHT = 15;

    Point region1_pointA = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y);
    Point region1_pointB = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION1_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION1_HEIGHT);

    //200, 150 right
    static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(205, 175);

    static final int REGION2_WIDTH = 15;
    static final int REGION2_HEIGHT = 15;
    Point region2_pointA = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x,
            REGION2_TOPLEFT_ANCHOR_POINT.y);
    Point region2_pointB = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x + REGION2_WIDTH,
            REGION2_TOPLEFT_ANCHOR_POINT.y + REGION2_HEIGHT);

    /*
     * Working variables
     */
    Mat region1_Cb;
    Mat region1_Cr;

    Mat region2_Cb;
    Mat region2_Cr;

    Mat YCrCb = new Mat();
    Mat Cb = new Mat();
    Mat Cr = new Mat();

    int avgCb1;
    int avgCr1;
    int avgCb2;
    int avgCr2;

    /*
     * This function takes the RGB frame, converts to YCrCb,
     * and extracts the Cb channel to the 'Cb' variable
     */
    void inputToCb(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cb, 1);
    }
    void inputToCr(Mat input){
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cr, 2);
    }


    public void init(Mat input) {

        inputToCb(input);
        inputToCr(input);

        region1_Cr = Cr.submat(new Rect(region1_pointA, region1_pointB));
        region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        region2_Cr = Cr.submat(new Rect(region2_pointA, region2_pointB));
        region2_Cb = Cb.submat(new Rect(region2_pointA, region2_pointB));
    }

    @Override
    public Mat processFrame(Mat input) {
        inputToCb(input);
        inputToCr(input);

        avgCb1 = (int) Core.mean(region1_Cb).val[0];
        avgCr1 = (int) Core.mean(region1_Cr).val[0];
        avgCb2 = (int) Core.mean(region2_Cb).val[0];
        avgCr2 = (int) Core.mean(region2_Cr).val[0];

        // draw rectangles on camera screen
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

        if (avgCb1 > avgCr1 + 50 || avgCb1 + 50 < avgCr1){
            position = "Center";
            telemetry.addData("Position", "Center");

        }
        else if (avgCb2 > avgCr2 + 50 || avgCb2 + 50 < avgCr2){
            position = "Right";
            telemetry.addData("Position", "Right");
        }
        else{
            position = "Left";
            telemetry.addData("Position", "Left");
        }
        telemetry.update();

        return input;
    }

    public String getPosition() {
        return position;
    }

}