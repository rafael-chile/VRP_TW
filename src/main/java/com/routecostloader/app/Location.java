package com.routecostloader.app;


public class Location {
    private double location_lat;
    private double location_lng;

    private Location(){}

    public Location(double latitude, double longitude){
        this.location_lat = latitude;
        this.location_lng = longitude;
    }

    public Location(String latitude, String longitude){
        this.location_lat = Double.parseDouble(latitude);
        this.location_lng = Double.parseDouble(longitude);
    }


    public double getLocation_lat() {
        return location_lat;
    }

    public double getLocation_lng() {
        return location_lng;
    }


    @Override
    public String toString() {
        return "{\"latLng\":{\"lat\":"+location_lat+",\"lng\":"+location_lng+"}}".replace("\\","");
    }
}
