package com.vrptw.entities;

import java.util.Date;

public class Client {
    private String idClient;
    private String localidade_postal;
    private Date tw_from;
    private Date tw_to;
    private String location_lat;
    private String location_lng;

    public Client(){}

    public String getLocalidade_postal() {
        return localidade_postal;
    }

    public void setLocalidade_postal(String localidade_postal) {
        this.localidade_postal = localidade_postal;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public Date getTw_from() {
        return tw_from;
    }

    public void setTw_from(Date tw_from) {
        this.tw_from = tw_from;
    }

    public Date getTw_to() {
        return tw_to;
    }

    public void setTw_to(Date tw_to) {
        this.tw_to = tw_to;
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

    @Override
    public String toString() {
        return "Client: {"
                + "idClient: " + idClient
                + ", localidade_postal: " + localidade_postal
                + ", tw_from: " + tw_from
                + ", tw_to " + tw_to
                + ", location_lat " + location_lat
                + ", location_lng " + location_lng
                + "}";
    }
}
