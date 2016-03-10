package com.vrptw.entities;


public class Location {
    private String idLocation;
    private String location_lat;
    private String location_lng;
    private String google_map;
    private String main_location;
    private String is_real;
    private String client;

    public Location(){}

    public Location(String idLocation, String latitude, String longitude, String googleMapLoc,
                    String isMainLocation,String isReal, String client){

        this.idLocation = idLocation;
        this.location_lat = latitude;
        this.location_lng = longitude;
        this.google_map = googleMapLoc;
        this.main_location = isMainLocation;
        this.is_real = isReal;
        this.client = client;
    }

    public String getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public String getLocation_lng() {
        return location_lng;
    }

    public void setLocation_lng(String location_lng) {
        this.location_lng = location_lng;
    }

    public String getGoogle_map() {
        return google_map;
    }

    public void setGoogle_map(String google_map) {
        this.google_map = google_map;
    }

    public String getMain_location() {
        return main_location;
    }

    public void setMain_location(String main_location) {
        this.main_location = main_location;
    }

    public String getIs_real() {
        return is_real;
    }

    public void setIs_real(String is_real) {
        this.is_real = is_real;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }



    @Override
    public String toString() {
        return "Location: {"
                + "idLocation: " + idLocation
                + ", location_lat: " + location_lat
                + ", location_lng: " + location_lng
                + ", google_map: " + google_map
                + ", main_location: " + main_location
                + ", is_real: " + is_real
                + ", client: " + client
                + "}";
    }
}
