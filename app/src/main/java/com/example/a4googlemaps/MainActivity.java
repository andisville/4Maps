package com.example.a4googlemaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_ACCESS_FINE_LOCATION = 123;

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager m;
    private String provider;
    private ArrayList<MyPos> posList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /// TODO: 12.03.2019 if necessary read file and add previous Marker
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: 12.03.2019 write posList to File
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

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if ((requestCode == PERMISSIONS_ACCESS_FINE_LOCATION) && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            run();
        }
    }

    private void run() throws SecurityException {

        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                System.out.println("onStatusChanged()\n");
            }

            @Override
            public void onProviderEnabled(String provider) {
                System.out.println("onProviderEnabled()\n");
            }

            @Override
            public void onProviderDisabled(String provider) {
                System.out.println("onProviderDisabled()\n");
            }

            @Override
            public void onLocationChanged(Location location) {
                setMarker(location, BitmapDescriptorFactory.HUE_YELLOW);
                if (posList.size() > 0)
                    setPolyline(posList.get(posList.size() - 1), location);
                posList.add(new MyPos(Calendar.getInstance().getTime(), location.getLatitude(), location.getLongitude(), location.getAltitude()));
                setCamera(location);
            }
        };

        m = getSystemService(LocationManager.class);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = m.getBestProvider(criteria, true);

        m.requestLocationUpdates(provider, 3000, 0, locationListener);
        Location location = m.getLastKnownLocation(provider);
        if (location != null) {
            posList.add(new MyPos(Calendar.getInstance().getTime(), location.getLatitude(), location.getLongitude(), location.getAltitude()));
            setMarker(location, BitmapDescriptorFactory.HUE_GREEN);
            setCamera(location);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    private void setPolyline(MyPos prevLocation, Location actLocation) {
        mMap.addPolyline(new PolylineOptions().add(new LatLng(actLocation.getLatitude(), actLocation.getLongitude()),
                new LatLng(prevLocation.getLatitude(), prevLocation.getLongitude())).width(5));
    }

    private void setMarker(Location location, float color) {
        if (location != null) {
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions marker = new MarkerOptions();
            marker.position(pos).title(String.valueOf(Calendar.getInstance().getTime()));
            marker.icon(BitmapDescriptorFactory.defaultMarker(color));

            mMap.addMarker(marker);
        }
    }

    private void setCamera(Location loc) {
        if (loc != null) {
            LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraUpdate cuPos = CameraUpdateFactory.newLatLng(pos);
            //CameraUpdate cuZoom = CameraUpdateFactory.zoomTo(15);

            //mMap.animateCamera(cuZoom);
            mMap.animateCamera(cuPos, 5000, null);
        }
    }
}