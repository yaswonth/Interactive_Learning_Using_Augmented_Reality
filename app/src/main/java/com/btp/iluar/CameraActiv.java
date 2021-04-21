package com.btp.iluar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.aruco.Aruco;
import org.opencv.aruco.DetectorParameters;
import org.opencv.aruco.Dictionary;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.*;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import aruco.CameraParameters;
import aruco.Marker;
import aruco.MarkerDetector;

public class CameraActiv extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    Mat mat1,mat2,dstMat,resultMat;
    Point p1,p2,p3,p4,pc1,pc2,pc3,pc4;
    private static int SPLASH_SCREEN_TIME_OUT=1000;
    boolean xr = false;
    int h,w;
    private String fii;
    private int alph = 0;
    private int[] dlist = {0, 3, 8, 13, 17, 21, 25, 27, 30, 32, 34, 37, 40, 43, 45, 47, 52, 54, 58, 62, 65, 67, 69, 71, 73, 75, 77};
    private String[] mlist = {"apple.obj", "ant.obj", "axe.obj", "book.obj", "bell.obj", "butter.obj", "ball.obj", "banana.obj", "cake.obj", "car.obj", "cat.obj", "chair.obj", "carro.obj", "dog.obj", "dolphin.obj", "duck.obj", "Donut2.obj", "ea.obj", "egg.obj", "elephant.obj", "ear.obj", "frouug.obj", "fan.obj", "fox.obj", "fish.obj", "gift.obj", "gold.obj", "hammer.obj", "house.obj", "hat.obj", "ice.obj", "iron.obj", "jeans.obj", "jug.obj", "key.obj", "knife.obj", "kite.obj", "Lock.obj", "lemon.obj", "ladder.obj", "mushroom.obj", "monkey.obj", "mobi.obj", "nuts.obj", "na.obj", "ora.obj", "ow.obj", "pine.obj", "pizza.obj", "pen.obj", "pig.obj", "pump.obj", "yellow.obj", "yellow.obj", "rose.obj", "rat.obj", "rocket.obj", "ring.obj", "santa.obj", "snow.obj", "snake.obj", "shoes.obj", "tur.obj", "train.obj", "teddy.obj", "uni.obj", "umb.obj", "violin.obj", "van.obj", "watch.obj", "wheel.obj", "yellow.obj", "yellow.obj", "yach.obj", "yellow.obj", "zodiac.obj", "zebra.obj"};
    private int[] tlist = {R.drawable.apple, R.drawable.ant, R.drawable.axe, R.drawable.book, R.drawable.bell, R.drawable.butter, R.drawable.ball, R.drawable.banana, R.drawable.cake, R.drawable.car, R.drawable.cat, R.drawable.chair, R.drawable.carrot, R.drawable.imo, R.drawable.dolphin, R.drawable.duck, R.drawable.donut, R.drawable.fogel, R.drawable.ball, R.drawable.ele, R.drawable.ear, R.drawable.frog, R.drawable.fan, R.drawable.ia, R.drawable.fish, R.drawable.gift, R.drawable.gold, R.drawable.ham, R.drawable.ho, R.drawable.hat, R.drawable.ice, R.drawable.iron, R.drawable.jeans, R.drawable.jug, R.drawable.key, R.drawable.knife, R.drawable.kite, R.drawable.lock, R.drawable.lemon, R.drawable.mus, R.drawable.monkey, R.drawable.im, R.drawable.nuts, R.drawable.nail, R.drawable.c, R.drawable.ow, R.drawable.pineapple, R.drawable.pizza, R.drawable.pen, R.drawable.pig, R.drawable.pump, R.drawable.quest, R.drawable.queen, R.drawable.rose, R.drawable.rat, R.drawable.rocket, R.drawable.ring, R.drawable.santa, R.drawable.snow, R.drawable.snake, R.drawable.shoes, R.drawable.turtle, R.drawable.train, R.drawable.texbear, R.drawable.uni, R.drawable.umb, R.drawable.violin, R.drawable.van, R.drawable.watch, R.drawable.wheel, R.drawable.xray, R.drawable.xyl, R.drawable.yacht, R.drawable.yellow, R.drawable.zodiac, R.drawable.zebra};
    public Bitmap bmp = null;
    MatOfPoint2f approxCurve;
    private static final float MARKER_SIZE = (float) 0.017;
    private static final boolean SHOW_MARKERID = false;
    private Mat cameraMatrix;
    private MatOfDouble distorsionMatrix;
    ImageView imgv;

    public void toa( char t){
        Toast.makeText(this,"Alphabet: "+t,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imgv = (ImageView) findViewById(R.id.imgv);
        imgv.setVisibility(View.GONE);
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dp ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                Random random = new Random();
                int index = random.nextInt(26)+1;
                toa(dp.charAt(index-1));
                index = dlist[index-1] + random.nextInt(dlist[index]-dlist[index-1]);
                Intent ine = new Intent(CameraActiv.this,MainActivity2.class);
                ine.putExtra("charvalue",index);
                startActivity(ine);
            }
        });

        cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.cameraview);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        cameraMatrix = new Mat(3,3, CvType.CV_32FC1);
        distorsionMatrix = new MatOfDouble();
        HashMap<Integer, Double> matricies = new HashMap<Integer, Double>();
        String line = "1.019099074177694320e+03,0.000000000000000000e+00,6.557727729771451095e+02,0.000000000000000000e+00,1.011927236550148677e+03,3.816077913964442700e+02,0.000000000000000000e+00,0.000000000000000000e+00,1.000000000000000000e+00";

        String[] calibDataLine = line.split(",");
        for (int i=0;i<calibDataLine.length;i++){
            double matrixValue = Double.parseDouble(calibDataLine[i]);
            matricies.put(i, matrixValue);
        }

        String line1 = "2.576784605153304430e-01,-1.300640184051879311e+00,-4.285777480424158084e-03,-2.507657388926626523e-03,2.307018624520866812e+00";

        String[] calibDataLine1 = line1.split(",");
        for (int i=0;i<calibDataLine1.length;i++){
            double matrixValue = Double.parseDouble(calibDataLine1[i]);
            matricies.put(i+9, matrixValue);
        }



        cameraMatrix.put(0,0, matricies.get(0), matricies.get(1), matricies.get(2),
                matricies.get(3), matricies.get(4), matricies.get(5),
                matricies.get(6), matricies.get(7), matricies.get(8));


        double[] distArray =  {matricies.get(9),
                matricies.get(10),
                matricies.get(11),
                matricies.get(12),
                matricies.get(13)};
        distorsionMatrix.fromArray(distArray);

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch (status){
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
//        toa();

    }

    public int biggestArea(List<MatOfPoint> contours){
        double maxArea = -1;
        int maxAreaIdx = -1;
        Log.d("size",Integer.toString(contours.size()));
        MatOfPoint temp_contour = contours.get(0); //the largest is at the index 0 for starting point
        approxCurve = new MatOfPoint2f();
        MatOfPoint largest_contour = contours.get(0);

        List<MatOfPoint> largest_contours = new ArrayList<MatOfPoint>();

        for (int idx = 0; idx < contours.size(); idx++) {
            temp_contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(temp_contour);
            //compare this contour to the previous largest contour found
            if (contourarea >= maxArea) {
                //check if this contour is a square
                MatOfPoint2f new_mat = new MatOfPoint2f( temp_contour.toArray() );
                int contourSize = (int)temp_contour.total();
                MatOfPoint2f approxCurve_temp = new MatOfPoint2f();
                Imgproc.approxPolyDP(new_mat, approxCurve_temp, contourSize*0.05, true);
                if (approxCurve_temp.total() == 4) {
                    maxArea = contourarea;
                    maxAreaIdx = idx;
                    approxCurve=approxCurve_temp;
                    double[] temp_double;
                    temp_double = approxCurve.get(0,0);
                    pc1 = new Point(temp_double[0], temp_double[1]);
                    temp_double = approxCurve.get(1,0);
                    pc2 = new Point(temp_double[0], temp_double[1]);
                    temp_double = approxCurve.get(2,0);
                    pc3 = new Point(temp_double[0], temp_double[1]);
                    temp_double = approxCurve.get(3,0);
                    pc4 = new Point(temp_double[0], temp_double[1]);
                    List<Point> points = new ArrayList<Point>();
                    points.add(pc1);
                    points.add(pc2);
                    points.add(pc3);
                    points.add(pc4);
                    Collections.sort(points, new Comparator<Point>() {
                        @Override
                        public int compare(Point p1, Point p2) {
                            double s1 = p1.x + p1.y;
                            double s2 = p2.x + p2.y;
                            return Double.compare(s1, s2);
                        }
                    });
                    Point topLeft = points.get(0);
                    Point bottomRight = points.get(3);


                    // # now, compute the difference between the points, the
                    // # top-right point will have the smallest difference,
                    // # whereas the bottom-left will have the largest difference
                    Collections.sort(points, new Comparator<Point>() {
                        @Override
                        public int compare(Point p1, Point p2) {
                            double s1 = p1.y - p1.x  ;
                            double s2 = p2.y - p2.x;
                            return Double.compare(s1, s2);
                        }
                    });
                    Point topRight = points.get(0);
                    Point bottomLeft = points.get(3);
                    p1 = topLeft;
                    p2 = topRight;
                    p3 = bottomRight;
                    p4 = bottomLeft;
                    xr = true;


                }
            }
        }

        return maxAreaIdx;
    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat frame = inputFrame.rgba();
        Mat arfr = inputFrame.gray();
        mat2 = frame.clone();
        Imgproc.cvtColor(frame,mat1,Imgproc.COLOR_RGBA2GRAY);

        Imgproc.GaussianBlur(mat1,mat1,new Size(5,5),1);
        Imgproc.Canny(mat1,mat1,50,255);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) + 1, (2*2)+1));
        Imgproc.dilate(mat1,mat1,kernel,new Point(0,0),2);
        Imgproc.erode(mat1,mat1,kernel,new Point(0,0),1);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(mat1,contours,new Mat(),Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat hierarchey = new Mat();
        Scalar colorblue = new Scalar(0, 0, 255);
        Scalar colorred = new Scalar(0, 255, 0);
        Scalar colorfr = new Scalar(0, 255, 255);
        Scalar colorgr = new Scalar(255, 255, 0);
        xr = false;
        Mat gra = new Mat();
        if (contours.size()>0){
            int maxAreaIdx = biggestArea(contours);
            if (xr){
                System.out.println("found!!!");
                Mat srcMat = new Mat(4, 1, CvType.CV_32FC2);
                srcMat.put(0,0,pc1.x,pc1.y,pc2.x,pc2.y,pc3.x,pc3.y,pc4.x,pc4.y);

                Mat M = Imgproc.getPerspectiveTransform(srcMat,dstMat);
                Mat img = null;
                try {
                    img = Utils.loadResource(this, R.drawable.fin, CvType.CV_8UC4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (img!=null){
                    Imgproc.warpPerspective(mat2,img,M,img.size());
//                    toa();
                    System.out.println(img);
                    bmp=null;
                    try {
                        //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
                        bmp = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(img, bmp);
                    }
                    catch (CvException e){Log.d("Exception",e.getMessage());}
                    if (bmp!=null){
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                imgv.setImageBitmap(bmp);
                                imgv.setVisibility(View.VISIBLE);
                                // Stuff that updates the UI

                            }
                        });

                    }
//                    Intent ine = new Intent(CameraActiv.this,MainActivity2.class);
//                    startActivity(ine);

                }

            }
        }

        return mat2;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
      mat1 = new Mat(width,height, CvType.CV_8UC4);
        mat2 = new Mat(width,height, CvType.CV_8UC4);
        dstMat = new Mat(4, 1, CvType.CV_32FC2);
        System.out.println("dimensions");
        System.out.println(width);
        System.out.println(height);
        dstMat.put(0, 0,0.0,0.0,630.0,0.0,630.0,888.0,0.0,888.0);
        h = height;
        w = width;




    }


    @Override
    public void onCameraViewStopped() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Opencv failed.",Toast.LENGTH_SHORT).show();
        }else{
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}