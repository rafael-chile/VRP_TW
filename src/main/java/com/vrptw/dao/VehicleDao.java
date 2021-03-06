package com.vrptw.dao;

import com.vrptw.entities.Vehicle;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class VehicleDao extends GenericDao{

    @SuppressWarnings(value = "unchecked")
    public List<Vehicle> getList() throws SQLException {
        String sqlQuery = "SELECT * FROM vehicles";
        List<Vehicle> dataList = null;
        try {
            dataList = this.read(Vehicle.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //dataList.stream().forEach(System.out::println);
        return dataList ;
    }


/*    public List<Vehicle> getList() throws SQLException{
        String query = "SELECT * FROM vehicles";
        return getList(query);
    }*/

    /*
     *  WARNING: the parameter resulset of the executed query passed by parameter could no match with the parsed data
     *  in this method, so in case any modification it should be also updated.
     * @param query the query that will be run
     * @return the list of Vehicles found in DB
     */
  /*  public List<Vehicle> getList(String query) throws SQLException{
        List<Vehicle> vehicleLst = new ArrayList<>();
        Connection con = connectionManager.connect();

        // Execute a query
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Extract data from result set
        while (rs != null && rs.next()) {
            String idVehicle = rs.getString("idVehicle");
            String licensePlate = rs.getString("license_plate");
            String maxWeight = rs.getString("max_weight");
            String description = rs.getString("description");
            String driverType = rs.getString("driver_type");

            Vehicle vh = new Vehicle(idVehicle, licensePlate, maxWeight, description, driverType);
            vehicleLst.add(vh);
        }

        //finally block used to close resources
        try {
            // Clean-up environment
            stmt.close();
            if (rs != null ){ rs.close(); }

            connectionManager.disconnect(); // don't disconnect using "con.close()" form
        } catch (SQLException ignored) {}// nothing we can do


        return vehicleLst;
    }*/


}
