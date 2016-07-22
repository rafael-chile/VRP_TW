package com.routecostloader.app;

import java.io.Serializable;

public class JsonToPost implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;

    private String fromClient;
    private String toClient;
    private String fromLocation;
    private String toLocation;
    private String jsonRequest;
    private String jsonResponse;


    private JsonToPost(){}

    public JsonToPost(String fromClient, String toClient, String fromLocation, String toLocation, String json){
        this.fromClient = fromClient;
        this.toClient = toClient;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.jsonRequest = json;
    }

    public String getFromClient() {
        return fromClient;
    }

    public String getToClient() {
        return toClient;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getJsonRequest() {
        return jsonRequest;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public String toString() {
        return ("JsonToPost=> From_Client:" + fromClient + ", To_Client:" + toClient + ", From_Location: " + fromLocation + ", To_Location: " + toLocation + " JSONRequest: " + jsonRequest);
    }
}
