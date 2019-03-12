package com.example.a4googlemaps;

import java.util.Date;

class MyMarker {

    private Date time;
    private double latitude;
    private double longitude;
    private double altitude;

    MyMarker(Date time, double latitude, double longitude, double altitude) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return time + ";" + latitude + ";" + longitude + ";" + altitude;
    }
}
