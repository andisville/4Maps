package com.example.a4googlemaps;

import java.util.Date;

class MyPos {

    private Date time;
    private double latitude;
    private double longitude;
    private double altitude;

    MyPos(Date time, double latitude, double longitude, double altitude) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return time + ";" + latitude + ";" + longitude + ";" + altitude;
    }

    public Date getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }
}
