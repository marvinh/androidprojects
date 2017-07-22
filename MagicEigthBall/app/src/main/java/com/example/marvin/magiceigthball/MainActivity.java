package com.example.marvin.magiceigthball;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static final  String [] ANSWERS =
            {
                    "It is certain",
                    "It is decidedly so",
                    "Without a doubt",
                    "Yes definitely",
                    "You may rely on it",
                    "As I see it yes",
                    "Most likely",
                    "Outlook good",
                    "Yes",
                    "Signs point to yes",
                    "Reply hazy try again",
                    "Ask again later",
                    "Better not tell you now",
                    "Cannot predict now",
                    "Concentrate and ask again",
                    "Don't count on it",
                    "My reply is no",
                    "My sources say no",
                    "Outlook not so good",
                    "Very doubtful"
            };

    private Random mRandom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shakeButton = (Button) findViewById(R.id.shakeButton);

        mRandom = new Random();

        shakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) findViewById(R.id.textView);
                String answer = ANSWERS[Math.abs(mRandom.nextInt()%20)];
                textView.setText(answer);
            }
        });

        SensorManager manager = (SensorManager) getApplicationContext().getSystemService(getApplicationContext().SENSOR_SERVICE);
        Sensor accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener listener = new SensorEventListener() {
            boolean enable = false;
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {


                if(sensorEvent.values[1] < -1.0 && enable && sensorEvent.values[2] < -5.0)
                {
                    TextView textView = (TextView) findViewById(R.id.textView);
                    String answer = ANSWERS[Math.abs(mRandom.nextInt()%20)];
                    textView.setText(answer);
                    enable = false;

                }
                if(sensorEvent.values[1] > 1.0)
                {
                    enable = true;
                }

                System.out.println(sensorEvent.values[1] + " "+ sensorEvent.values[2]);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        manager.registerListener(listener, accel, manager.SENSOR_DELAY_FASTEST);


        //manager.unregisterListener(listener);

    }
}
