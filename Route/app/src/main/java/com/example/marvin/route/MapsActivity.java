package com.example.marvin.route;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng mMark;
    int mRSSILevel = 0;
    TextView textView;
    long polyLineCounter = 0;
    PolylineOptions polylineOptions;
    ArrayList<PolylineOptions> ploList;

    ArrayList<LatLng> latLngArrayList;

    static final int LOWER_THRESH = -65;
    static final int HIGHER_THRESH = -60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        try {
            System.out.println(wifiManager.getScanResults());
        } catch (SecurityException e) {

        }



        textView = (TextView) findViewById(R.id.textView);


        Thread  thread = new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    wifiManager.startScan();
                                } catch (SecurityException e) {


                                }
                                mRSSILevel = wifiManager.getScanResults().get(0).level;
                                textView.setText(wifiManager.getScanResults().get(0).SSID +" RSSI level: " + mRSSILevel);
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMap.moveCamera(CameraUpdateFactory.zoomBy(20));

        polylineOptions = new PolylineOptions();

        polylineOptions.width(10);
        polylineOptions.color(Color.BLUE);

        ploList = new ArrayList<>();
        latLngArrayList = new ArrayList<>();

        LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {





                    mMark = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mMark));


                    if(mRSSILevel < LOWER_THRESH) { // Bad
                        mMap.addMarker(new MarkerOptions().position(mMark).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }

                    else if ( LOWER_THRESH <= mRSSILevel && mRSSILevel < HIGHER_THRESH) { // Ok
                        mMap.addMarker(new MarkerOptions().position(mMark).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    }

                    else { // Good
                        mMap.addMarker(new MarkerOptions().position(mMark).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    }

                    polylineOptions.add(mMark);


                    mMap.addPolyline(polylineOptions);





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
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }






    }

