package com.example.punekar.Models;

public class Locations {
    String place_name;
    Double latitude;
    Double longitude;

    public Locations(String place_name, Double latitude, Double longitude) {
        this.place_name = place_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
