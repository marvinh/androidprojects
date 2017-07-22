package com.example.marvin.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String mSensorString;
    private String mSensorString2;
    private String mSensorString3;
    private TextView textView;

    SensorEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        textView =  (TextView) findViewById(R.id.textView);


        listener = new SensorEventListener() {

            //Aaccel
            float minX;
            float minY;
            float minZ;

            float maxX;
            float maxY;
            float maxZ;


            ///Light
            float lmin = 10.0f;
            float lmax;


            //proximity;

            float pmin = 5.0f;
            float pmax;



            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    minX = Math.min(sensorEvent.values[0], minX);
                    minY = Math.min(sensorEvent.values[1], minY);
                    minZ = Math.min(sensorEvent.values[2], minZ);

                    maxX = Math.max(sensorEvent.values[0], maxX);
                    maxY = Math.max(sensorEvent.values[1], maxY);
                    maxZ = Math.max(sensorEvent.values[2], maxZ);

                    mSensorString = "Accelerometer m/s^2: \n";
                    mSensorString = mSensorString +
                            "x: " + sensorEvent.values[0] + "\nmaxX: " + maxX + "\nminX: " + minX + "\n" +
                            "y: " + sensorEvent.values[1] + "\nmaxY: " + maxY + "\nminY: " + minY + "\n" +
                            "z: " + sensorEvent.values[2] + "\nmaxZ: " + maxZ + "\nminZ: " + minZ + "\n";

                    TextView textView = (TextView) findViewById(R.id.sensorTextView);
                    textView.setText(mSensorString);

                    sensorManager.unregisterListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

                }
                else if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT)
                {

                    lmin = Math.min(sensorEvent.values[0], lmin);
                    lmax = Math.max(sensorEvent.values[0], lmax);


                    mSensorString2 = "Light Sensor lx: \n";
                    mSensorString2 = mSensorString2 +
                            "value: "+ sensorEvent.values[0] + "\nmax: " +lmax + "\nmin: "+ lmin + "\n";

                    TextView textView = (TextView) findViewById(R.id.sensorTextView2);
                    textView.setText(mSensorString2);

                    sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  sensorManager.SENSOR_DELAY_FASTEST);
                    sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), sensorManager.SENSOR_DELAY_FASTEST);

                }
                else if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)
                {
                    pmin = Math.min(sensorEvent.values[0], pmin);
                    pmax = Math.max(sensorEvent.values[0], pmax);


                    mSensorString3 = "Proximity cm: \n";
                    mSensorString3 = mSensorString3 +
                            "value: "+ sensorEvent.values[0] + "\nmax: " + pmax + "\nmin: "+ pmin + "\n";

                    TextView textView = (TextView) findViewById(R.id.sensorTextView3);
                    textView.setText(mSensorString3);

                    sensorManager.unregisterListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };


        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  sensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), sensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), sensorManager.SENSOR_DELAY_FASTEST);


        String sensorString = "Unavailable Sensors: \n";

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){

        }
        else {

            sensorString = "Acceleromter \n";

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null){

        }
        else {

            sensorString = sensorString + "Linear Accelerometer \n";

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null){

        }
        else {

            sensorString = sensorString + "Gravity sensor \n";

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null){

        }
        else {

            sensorString = sensorString + "Gyroscope \n";

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null){


        }
        else {

            sensorString = sensorString + "Ambient light sensor \n";
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){

        }
        else {

            sensorString = sensorString + "Ambient magnetic field sensor \n";

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null){


        }
        else {

            sensorString = sensorString + "Proximity sensor \n";

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null){

        }
        else {

            sensorString = sensorString + "Pressure sensor (barometer) \n";

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){

        }
        else {

            sensorString = sensorString + "Ambient temperature sensor \n";

        }


        textView.setText(sensorString);


    }



}
