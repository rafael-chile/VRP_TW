/**
 * Created by Usuario2 on 06/03/2016.
 */
public class Vehicule {
    String idVehicle;
    String licensePlate;
    String maxWeight;
    String description;
    String driverType;

    public Vehicule(String idVehicle, String licensePlate, String maxWeight, String description, String driverType){
        this.idVehicle = idVehicle;
        //todo. qgregqr cq;pos fqltqntes
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
}
