package com.routecostloader.app;

import java.io.Serializable;

public class JsonToPost implements Serializable{
    private String fromClient;
    private String toClient;
    private String json;

    private JsonToPost(){}

    public JsonToPost(String fromClient, String toClient, String json){
        this.fromClient = fromClient;
        this.toClient = toClient;
        this.json = json;
    }

    public String getFromClient() {
        return fromClient;
    }

    public String getToClient() {
        return toClient;
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toString() {
        return ("JsonToPost=> From_Client:" + fromClient + ", To_Client:" + toClient + ", JSON: " + json);
    }
}
