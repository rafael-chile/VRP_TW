package com.routecostloader.app;

import java.io.Serializable;

public class JsonToPost implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;

    private String fromClient;
    private String toClient;
    private String jsonRequest;
    private String jsonResponse;

    private JsonToPost(){}

    public JsonToPost(String fromClient, String toClient, String json){
        this.fromClient = fromClient;
        this.toClient = toClient;
        this.jsonRequest = json;
    }

    public String getFromClient() {
        return fromClient;
    }

    public String getToClient() {
        return toClient;
    }

    public String getJsonRequest() {
        return jsonRequest;
    }

    public String getJsonResponse() {
        return jsonRequest;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    @Override
    public String toString() {
        return ("JsonToPost=> From_Client:" + fromClient + ", To_Client:" + toClient + ", JSONRequest: " + jsonRequest);
    }
}
