package com.vrptw.entities;


public class Location {
    private String idLocation;
    private String location_lat;
    private String location_lng;
    private String google_map;
    private String main_location;
    private String is_real;
    private String client;
    private String radius_km;
    private String angleInDegrees;
    private String compass;
    private String rio_Tejo;


    public Location(){}

    public Location(String idLocation, String latitude, String longitude, String googleMapLoc,
                    String isMainLocation,String isReal, String client, String radius_km, String angleInDegrees, String compass, String rio_Tejo){

        this.idLocation = idLocation;
        this.location_lat = latitude;
        this.location_lng = longitude;
        this.google_map = googleMapLoc;
        this.main_location = isMainLocation;
        this.is_real = isReal;
        this.client = client;
        this.radius_km = radius_km;
        this.angleInDegrees = angleInDegrees;
        this.compass = compass;
        this.rio_Tejo = rio_Tejo;
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

    public String getIs_real() {  return is_real; }

    public void setIs_real(String is_real) { this.is_real = is_real;  }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRadius_km() {  return radius_km;  }

    public void setRadius_km(String radius_km) {  this.radius_km = radius_km;  }

    public String getAngleInDegrees() {    return angleInDegrees;  }

    public void setAngleInDegrees(String angleInDegrees) {  this.angleInDegrees = angleInDegrees;    }

    public String getCompass() {  return compass;  }

    public void setCompass(String compass) {  this.compass = compass;    }

    public String getRio_Tejo() {  return rio_Tejo;   }

    public void setRio_Tejo(String rio_Tejo) { this.rio_Tejo = rio_Tejo;  }


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
                + ", radius_km: " + radius_km
                + ", angleInDegrees: " + angleInDegrees
                + ", compass: " + compass
                + ", rio_Tejo: " + rio_Tejo
                + "}";
    }
}