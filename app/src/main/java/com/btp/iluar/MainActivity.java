package com.btp.iluar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Opencv loaded.",Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                        50); }else {
                Intent ine = new Intent(this,MainActivity2.class);
                startActivity(ine);
            }


        }else {
            Toast.makeText(getApplicationContext(),"Opencv failed.",Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onResume()
    {
        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause();
    }

}