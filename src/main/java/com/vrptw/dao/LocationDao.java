package com.vrptw.dao;

import com.vrptw.entities.Location;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocationDao extends GenericDao {

    public List<Location> getList() throws SQLException{
        String query = "SELECT * FROM locations";
        return getList(query);
    }

    /**
     *  WARNING: the parameter resulset of the executed query passed by parameter could no match with the parsed data
     *  in this method, so in case any modification it should be also updated.
     * @param query The query to be run
     * @return A list of Locations found in the DB
     */
    public List<Location> getList(String query) throws SQLException{
        List<Location> locationLst = new ArrayList<>();
        Connection con = connectionManager.connect();

        // Execute a query
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        for(Field f : Location.class.getDeclaredFields()){
            System.out.println("FieldName: " + f.getName());
        }

        // Extract data from result set
        while (rs != null && rs.next()) {
            String idLocation = rs.getString("idLocation");
            String latitude = rs.getString("location_lat");
            String longitude = rs.getString("location_lng");
            String googleMapLoc = rs.getString("google_Map");
            String isMainLocation = rs.getString("main_location");
            String isReal = rs.getString("is_Real");
            String client = rs.getString("client");

            Location location = new Location(idLocation, latitude, longitude, googleMapLoc, isMainLocation, isReal, client);
            locationLst .add(location);

           // System.out.println("Result added: " + longitude);
        }

        //finally block used to close resources
        try {
            // Clean-up environment
            stmt.close();
            if (rs != null ){ rs.close(); }

            connectionManager.disconnect(); // don't disconnect using "con.close()" form
        } catch (SQLException ignored) {}// nothing we can do


        return locationLst;
    }

}
