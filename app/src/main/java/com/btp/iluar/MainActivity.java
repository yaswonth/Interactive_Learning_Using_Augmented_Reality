package com.btp.iluar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MyOpenGLView myOpenGLView;
    private ObjLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myOpenGLView = new MyOpenGLView(this);


        if(OpenCVLoader.initDebug()){
//            Toast.makeText(getApplicationContext(),"Opencv loaded.",Toast.LENGTH_SHORT).show();
            try {
                loader = new ObjLoader(getAssets());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                System.out.println("loader failed");
            }
            myOpenGLView.setEGLContextClientVersion(3);
            myOpenGLView.setRenderer(new MyGLRenderer(this,loader));
        }else {
            Toast.makeText(getApplicationContext(),"Opencv failed.",Toast.LENGTH_SHORT).show();
        }
        setContentView(myOpenGLView);
    }
    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
        myOpenGLView.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
        myOpenGLView.onPause();
    }

}