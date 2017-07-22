package com.example.marvin.location;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    String currentText = "\tCurrent Location\n";
    String maxText = "\tMax Location\n";
    String minText = "\tMin Location\n";

    double minLatLong = 0.0;
    double maxLatLong = 0.0;

    double minLat = 0.0;
    double minLong = 0.0;

    double maxLat = 0.0;
    double maxLong = 0.0;

    double minAlt = 0.0;
    double maxAlt = 0.0;

    float minL = 0.0f;
    float maxL = 0.0f;
    float currL = 0.0f;

    boolean changedmaxL = false;
    boolean changedminL = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);

        System.out.println("LOCATIONLOCATION: "+locationManager.getAllProviders());


        final SensorEventListener sensorEventListener = new SensorEventListener() {

            String str;

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                currL = sensorEvent.values[0];
                    if(minL == 0.0f)
                    {
                        minL =  sensorEvent.values[0];
                        changedminL = true;
                    }else{
                        float temp = minL;
                        minL = Math.min(minL,sensorEvent.values[0]);
                        if(minL != temp)
                        {
                            changedminL = true;
                        }
                    }
                    if (maxL == 0.0f)
                    {
                        maxL = sensorEvent.values[0];
                        changedmaxL = true;
                    }else{
                        float temp = maxL;
                        maxL = Math.max(maxL,sensorEvent.values[0]);
                        if(maxL != temp)
                        {
                            changedmaxL = true;
                        }
                    }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),  sensorManager.SENSOR_DELAY_NORMAL);

        LocationListener locationListener = new LocationListener() {



            @Override
            public void onLocationChanged(Location location) {
                currentText = "\tCurrent Location\n";
                maxText = "\tMax Location\n";
                minText = "\tMin Location\n";

                if(changedminL)
                {
                    minLat = location.getLatitude();
                    minLong = location.getLongitude();
                    minAlt = location.getAltitude();
                    changedminL = false;
                }

                if(changedmaxL)
                {
                    maxLat = location.getLatitude();
                    maxLong = location.getLongitude();
                    maxAlt = location.getAltitude();
                    changedmaxL = false;
                }

                currentText = currentText + "\tLatitude: "+ location.getLatitude() + " °\n" +
                        "\tLongitude: "+ location.getLongitude() + " °\n"+
                        "\tAltitude: "+ location.getAltitude() + " m\n"+
                        "\tLight: "+ currL + " lx\n";

                try {


                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if(addressList.size() > 0) {
                        currentText = currentText + "\tLocation name: \n\t" + addressList.get(0).getAddressLine(0) + "\n\t" +
                                addressList.get(0).getSubLocality() + ", " + addressList.get(0).getAdminArea() + ", " + addressList.get(0).getPostalCode() + "\n";
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

                maxText = maxText+ "\tLatitude: "+ maxLat + " °\n" +
                        "\tLongitude: "+ maxLong + " °\n"+
                        "\tAltitude: "+ maxAlt + " m\n" +
                        "\tLight: "+ maxL + " lx\n";

                try {

                    List<Address> addressList = geocoder.getFromLocation(maxLat, maxLong, 1);
                    if(addressList.size() > 0) {
                        maxText = maxText + "\tLocation name: \n\t" + addressList.get(0).getAddressLine(0) + "\n\t" +
                                addressList.get(0).getSubLocality() + ", " + addressList.get(0).getAdminArea() + ", " + addressList.get(0).getPostalCode() + "\n";
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }


                minText = minText+ "\tLatitude: "+ minLat + " °\n" +
                        "\tLongitude: "+ minLong + " °\n"+
                        "\tAltitude: "+ minAlt + " m\n"+
                        "\tLight: "+ minL + " lx\n";

                try {

                    List<Address> addressList = geocoder.getFromLocation(minLat, minLong, 1);
                    if(addressList.size() > 0) {
                        minText = minText + "\tLocation name: \n\t" + addressList.get(0).getAddressLine(0) + "\n\t" +
                                addressList.get(0).getSubLocality() + ", " + addressList.get(0).getAdminArea() + ", " + addressList.get(0).getPostalCode() + "\n";
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }


                TextView textView = (TextView) findViewById(R.id.locationTextView);

                String temp = currentText+"\n"+maxText+"\n"+minText;
                textView.setText(temp);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };




        try {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }catch (SecurityException e)
        {
            e.printStackTrace();
        }



    }
}
