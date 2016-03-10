package com.vrptw;

import com.vrptw.dao.LocationDao;
import com.vrptw.dao.VehicleDao;
import com.vrptw.entities.Location;
import com.vrptw.entities.Vehicle;

import java.sql.SQLException;
import java.util.List;

public class FirstExample {


    public static void main(String[] args) {

        VehicleDao vehicleDao = new VehicleDao();
        LocationDao locationDao = new LocationDao();

        try {
            List<Vehicle> allVechicles = vehicleDao.getList();
            List<Location> alllocations = locationDao.getList();

            // NOTE: the datatype of max_weight in the db is varchar, in which case a query like "where max_weight > 50"
            // TODO: change the data type to Integer (which involves modify the Vehicle DAO class)
            List<Vehicle> vechiclesFiltered = vehicleDao.getList("SELECT * from vehicles where max_weight = 400");


            System.out.println("Complete vehicles resulset count:" + allVechicles.size());
            System.out.println("Complete locations resulset count:" + allVechicles.size());

            System.out.println("Filtered vehicles resulset count:" + vechiclesFiltered.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Goodbye!");

    }//end main

}