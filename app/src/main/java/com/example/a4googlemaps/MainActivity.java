package com.example.a4googlemaps;

import android.Manifest;
import android.content.pm.PackageManager;
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

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_ACCESS_FINE_LOCATION = 123;
    enum MarkerType{START, INTERMEDIATE, GOAL}

    private GoogleMap mMap;
    private LocationListener locationListener;
    String provider;
    private int markCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
            }
        };

        LocationManager m = getSystemService(LocationManager.class);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = m.getBestProvider(criteria, true);

        m.requestLocationUpdates(provider, 3000, 0, locationListener);
        Location location = m.getLastKnownLocation(provider);
        if (location != null) {
            setMarker(location, BitmapDescriptorFactory.HUE_GREEN);
            setCamera(location);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    private void setMarker(Location location, float color) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions();
        options.position(pos).title(String.valueOf(markCounter));
        options.icon(BitmapDescriptorFactory.defaultMarker(color));
        mMap.addMarker(options);
        markCounter++;
    }

    private void setCamera(Location loc) {
        if (loc != null) {
            LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
            CameraUpdate cu3 = CameraUpdateFactory.newLatLng(pos);
            mMap.animateCamera(cu3, 5000, null);
        }
    }
}