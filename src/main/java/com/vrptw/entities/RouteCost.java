package com.vrptw.entities;

public class RouteCost {

    private String idroute_cost;
    private String idclient_from;
    private String idclient_to;
    private double time_cost;
    private double distance_cost;
    private String idlocation_from;
    private String idlocation_to;
    private String comments;


    public RouteCost(){}

    public RouteCost(/*String idroute_cost,*/ String idclient_from, String idclient_to, double time_cost, double distance_cost, String idlocation_from, String idlocation_to, String comments){
        //this.idroute_cost = idroute_cost;
        this.idclient_from = idclient_from;
        this.idclient_to = idclient_to;
        this.time_cost = time_cost;
        this.distance_cost = distance_cost;
        this.idlocation_from = idlocation_from;
        this.idlocation_to = idlocation_to;
        this.comments = comments;
    }

    public String getIdroute_cost() {
        return idroute_cost;
    }

    public void setIdroute_cost(String idroute_cost) {
        this.idroute_cost = idroute_cost;
    }

    public String getIdclient_from() {
        return idclient_from;
    }

    public void setIdclient_from(String idclient_from) {
        this.idclient_from = idclient_from;
    }

    public String getIdclient_to() {
        return idclient_to;
    }

    public void setIdclient_to(String idclient_to) {
        this.idclient_to = idclient_to;
    }

    public double getTime_cost() {
        return time_cost;
    }

    public void setTime_cost(double time_cost) {
        this.time_cost = time_cost;
    }

    public double getDistance_cost() {
        return distance_cost;
    }

    public void setDistance_cost(double distance_cost) {
        this.distance_cost = distance_cost;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    public String getIdlocation_from() {
        return idlocation_from;
    }

    public void setIdlocation_from(String idlocation_from) {
        this.idlocation_from = idlocation_from;
    }

    public String getIdlocation_to() {
        return idlocation_to;
    }

    public void setIdlocation_to(String idlocation_to) {
        this.idlocation_to = idlocation_to;
    }


    @Override
    public String toString() {
        return "RouteCost: {"
                + "idroute_cost: " + idroute_cost
                + ", idclient_from: " + idclient_from
                + ", idclient_to: " + idclient_to
                + ", idlocation_from: " + idlocation_from
                + ", idlocation_to: " + idlocation_to
                + ", time_cost: " + time_cost
                + ", distance_cost: " + distance_cost
                + ", comments: " + comments
                + "}";
    }

    public String getInsertQery(){
        return
                ("INSERT INTO route_costs (idclient_from,idclient_to,idlocation_from,idlocation_to,time_cost,distance_cost) " +
                        " VALUES (" +
                            " \"" + idclient_from+"\"" +
                            ", \"" + idclient_to+"\"" +
                            ", \"" + idlocation_from+"\"" +
                            ", \"" + idlocation_to+"\"" +
                            ", " + time_cost+"" +
                            ", " + distance_cost+"" +
                            ");").replace("\\","");

    }

}
