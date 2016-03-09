package com.vrptw;

import com.vrptw.dao.ConnectionManager;
import com.vrptw.entities.Vehicule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FirstExample {


    public static void main(String[] args) {
        ConnectionManager cm = null;
        Connection conn;
        Statement stmt = null;
        List<Vehicule> vehiculeLst = new ArrayList<>();

        try{
            cm = new ConnectionManager();
            conn = cm.connect();

            // Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM vehicles";
            ResultSet rs = stmt.executeQuery(sql);


            // Extract data from result set
            while (rs.next()){
                String idVehicle = rs.getString("idVehicle");
                String licensePlate = rs.getString("license_plate");
                String maxWeight = rs.getString("max_weight");
                String description = rs.getString("description");
                String driverType = rs.getString("driver_type");

                Vehicule vh = new Vehicule(idVehicle, licensePlate,maxWeight, description, driverType);
                vehiculeLst.add(vh);

                System.out.println("Result added: " + vh);

            }

            vehiculeLst.toString();

            // Clean-up environment
            rs.close();
            stmt.close();
            cm.disconnect();
        } catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt != null)
                    stmt.close();
            }catch(SQLException ignored){ }// nothing we can do

            if (cm != null) {
                cm.disconnect();
            }

        }//end try
        System.out.println("Goodbye!");

    }//end main

}//end com.vrptw.FirstExample