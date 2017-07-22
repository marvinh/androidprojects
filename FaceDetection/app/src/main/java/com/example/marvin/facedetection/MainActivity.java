package com.example.marvin.facedetection;






import android.content.Context;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    CameraSource cameraSource;

    SurfaceView surfaceView;

    private class FaceTracker extends Tracker<Face>
    {
        int sideCount = 2;
        int vertCount = 2;

        long prevstamp = 0;
        boolean setstamp = false;
        String str = "Status";
        float startingpoint = 0.0f;
        float noseY = 0.0f;
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detections, Face item)
        {





            //System.out.println("Euler Y: "+ item.getEulerY() + "Euler Z: "+ item.getEulerZ());


            //System.out.println("POSY: "+ item.getPosition().y);



            if(vertCount == 2)
            {
                startingpoint = item.getPosition().y;
                vertCount = 1;
            }

            if(vertCount == 1)
            {
                if(startingpoint < item.getPosition().y)
                {
                    startingpoint = item.getPosition().y;
                    vertCount = 0;
                }
            }

            if(vertCount == 0)
            {
                if(startingpoint > item.getPosition().y)
                {
                    str = "Shaking head up and down";
                    vertCount = 2;
                }
            }

            if(item.getEulerY() >= 12.0)
            {
                if(sideCount == 2)
                {
                    sideCount = 1;
                }else if(sideCount == 0){
                    str = "Shaking head right left...";
                    sideCount = 2;
                }
            }

            if(item.getEulerY() <= -12.0)
            {
                if(sideCount == 2)
                {
                    sideCount = 0;
                }else if(sideCount == 1){

                    str = "Shaking head left right...";
                    sideCount = 2;
                }

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    TextView textView = (TextView) findViewById(R.id.trackText);
                    textView.setText(str);

                }
            });

            System.out.println(str);

        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FaceDetector detector = new FaceDetector.Builder(getApplicationContext()).setProminentFaceOnly(true).setMode(1)
                .build();






        detector.setProcessor(
                new LargestFaceFocusingProcessor(
                        detector,
                        new FaceTracker()));


        cameraSource = new CameraSource.Builder(getApplicationContext(),detector).setRequestedFps(30.0f).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(640,480).build();






        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {


                try {
                    try {
                        cameraSource.start(surfaceView.getHolder());

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }

            }


            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }

        });








    }




}
