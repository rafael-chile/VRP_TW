package com.vrptw.entities;


public class Vehicule {
    private String idVehicle;
    private String licensePlate;
    private String maxWeight;
    private String description;
    private String driverType;

    public Vehicule(String idVehicle, String licensePlate, String maxWeight, String description, String driverType){
        this.idVehicle = idVehicle;
        this.licensePlate = licensePlate;
        this.maxWeight = maxWeight;
        this.description = description;
        this.driverType = driverType;
    }


    public String getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(String idVehicle) {
        this.idVehicle = idVehicle;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    @Override
    public String toString() {
        return "Vehicle: {"
                    + "idVehicle: " + idVehicle
                    + ", licensePlate: " + licensePlate
                    + ", maxWeight: " + maxWeight
                    + ", description: " + description
                    + ", driverType: " + driverType
                + "}";
    }
}
