package com.vrptw.entities;


public class Vehicle {
    private String idVehicle;
    private String license_plate;
    private String max_weight;
    private String description;
    private String driver_type;

    public Vehicle(){}

    public Vehicle(String idVehicle, String licensePlate, String maxWeight, String description, String driverType){
        this.idVehicle = idVehicle;
        this.license_plate = licensePlate;
        this.max_weight = maxWeight;
        this.description = description;
        this.driver_type = driverType;
    }


    public String getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(String idVehicle) {
        this.idVehicle = idVehicle;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String licensePlate) {
        this.license_plate = licensePlate;
    }

    public String getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(String maxWeight) {
        this.max_weight = maxWeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriver_type() {
        return driver_type;
    }

    public void setDriver_type(String driverType) {
        this.driver_type = driverType;
    }

    @Override
    public String toString() {
        return "Vehicle: {"
                    + "idVehicle: " + idVehicle
                    + ", license_plate: " + license_plate
                    + ", max_weight: " + max_weight
                    + ", description: " + description
                    + ", driver_Type: " + driver_type
                + "}";
    }
}
