package com.vrptw.entities;


public class Location {
    private String idLocation;
    private String latitude;
    private String longitude;
    private String googleMapLoc;
    private String isMainLocation;
    private String isReal;
    private String client;

    public Location(String idLocation, String latitude, String longitude, String googleMapLoc,
                    String isMainLocation,String isReal, String client){

        this.idLocation = idLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.googleMapLoc = googleMapLoc;
        this.isMainLocation = isMainLocation;
        this.isReal = isReal;
        this.client = client;
    }

    public String getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGoogleMapLoc() {
        return googleMapLoc;
    }

    public void setGoogleMapLoc(String googleMapLoc) {
        this.googleMapLoc = googleMapLoc;
    }

    public String getIsMainLocation() {
        return isMainLocation;
    }

    public void setIsMainLocation(String isMainLocation) {
        this.isMainLocation = isMainLocation;
    }

    public String getIsReal() {
        return isReal;
    }

    public void setIsReal(String isReal) {
        this.isReal = isReal;
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
                + ", latitude: " + latitude
                + ", longitude: " + longitude
                + ", googleMapLoc: " + googleMapLoc
                + ", isMainLocation: " + isMainLocation
                + ", isReal: " + isReal
                + ", client: " + client
                + "}";
    }
}
