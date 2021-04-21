package com.btp.iluar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private GLSurfaceView mGLSurfaceView;
    private ObjLoader loader,bloader;
    LessonFourRenderer renderer;
    Point p1,p2,p3,p4,pc1,pc2,pc3,pc4;
    Mat mat1,mat2,dstMat,resultMat;
    boolean xr = false;
    int h,w;
    MatOfPoint2f approxCurve;
    private static final float MARKER_SIZE = (float) 0.017;
    private static final boolean SHOW_MARKERID = false;
    private Mat cameraMatrix;
    private MatOfDouble distorsionMatrix;
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    float wa = 630.0f;
    float ha = 888.0f;
    float rat = wa/ha;
    float diag;
    int index;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGLSurfaceView = new GLSurfaceView(this);
        RelativeLayout re = (RelativeLayout) findViewById(R.id.la);
        Intent ine = getIntent();
        index = ine.getIntExtra("charvalue",0);

        // Check if the system supports OpenGL ES 3.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x30000;

        cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.cameraview);
//        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

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

        if (supportsEs2)
        {
            // Request an OpenGL ES 3.0 compatible context.
            mGLSurfaceView.setEGLContextClientVersion(3);
            mGLSurfaceView.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
            mGLSurfaceView.getHolder().setFormat( PixelFormat.TRANSLUCENT );

            // Set the renderer to our demo renderer, defined below.
            try {
                loader = new ObjLoader(getAssets());
                bloader = new ObjLoader(getAssets());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("loader failed");
            }
            renderer=new LessonFourRenderer(this,loader,bloader,index);
            mGLSurfaceView.setRenderer(renderer);
            mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
        else
        {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return;
        }
        re.addView(mGLSurfaceView);
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
//                Mat srcMat = new Mat(4, 1, CvType.CV_32FC2);
//                srcMat.put(0,0,pc1.x,pc1.y,pc2.x,pc2.y,pc3.x,pc3.y,pc4.x,pc4.y);
                float diag_new = (float) Math.sqrt(Math.pow((p1.x-p3.x),2) + Math.pow(p1.y-p3.y,2));
                float tranz = (diag/diag_new)*5;
                float mid_x = (float) (p1.x + p3.x)/2 - (float) w/2;
                float mid_y = (float) (p1.y + p3.y)/2 - (float) h/2;
                float tran_x = (tranz*mid_x)/100;
                float tran_y = (tranz*mid_y)/100;
                System.out.println(diag_new);
                System.out.println(diag);
                renderer.settran(tran_x,tran_y,-1*tranz);






//                Mat M = Imgproc.getPerspectiveTransform(dstMat,srcMat);
//                Mat bl = new Mat() ;
//                bl = Mat.zeros(new Size(w,h),CvType.CV_8UC4);
//                Imgproc.resize(bl,bl,new Size(w,h));
//                List<MatOfPoint> contoursbig = new ArrayList<MatOfPoint>();
//                contoursbig.add(contours.get(maxAreaIdx));
//                Imgproc.fillPoly(bl,contoursbig,new Scalar(255,255, 255));
//                System.out.println(mat2);
//                System.out.println(bl);
//                Mat img = null;
//                try {
//                    img = Utils.loadResource(this, R.drawable.fin, CvType.CV_8UC4);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (img!=null){
//                    resultMat = mat2.clone();
//                    System.out.println(resultMat);
//                    Imgproc.warpPerspective(img,resultMat,M,frame.size());
//                    try {
//                        Dictionary dic = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_100);
//                        System.out.println("aruco running");
//                        System.out.println(dic);
//                        List<Mat> corners = new ArrayList<>();
//                        Mat ids = new Mat();
//
////                        Imgproc.cvtColor(arfr,gra,Imgproc.COLOR_RGBA2GRAY);
//                        Aruco.detectMarkers(arfr,dic,corners,ids);
//                        System.out.println(ids);
//                        System.out.println(corners);
//                        if (corners.size()>0){
//                           System.out.println("aruco marker found.");
//                           System.out.println(corners.get(0));
//
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    return arfr;
//                }

            }
        }
        mGLSurfaceView.requestRender();
        return frame;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mat1 = new Mat(width,height, CvType.CV_8UC4);
        mat2 = new Mat(width,height, CvType.CV_8UC4);
        h = height;
        w = width;
        diag = (float) Math.sqrt(Math.pow(h*rat,2) + Math.pow(h,2));
    }


    @Override
    public void onCameraViewStopped() {

    }



    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Opencv failed.",Toast.LENGTH_SHORT).show();
        }else{
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}