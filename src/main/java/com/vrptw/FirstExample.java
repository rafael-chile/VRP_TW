package com.vrptw;

import com.vrptw.dao.ClientDao;
import com.vrptw.dao.LocationDao;
import com.vrptw.dao.VehicleDao;
import com.vrptw.entities.Clients;
import com.vrptw.entities.Location;
import com.vrptw.entities.Vehicle;
import com.vrptw.forms.VRPTW;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class FirstExample {


    @SuppressWarnings(value = "unchecked")
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
            System.out.println("Complete locations resulset count:" + alllocations.size());

            System.out.println("Filtered vehicles resulset count:" + vechiclesFiltered.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Other example using a very generic DAO
        try {
            List l = (new LocationDao()).runQuery(Location.class, "SELECT * from locations");
            //l.stream().forEach(System.out::println);

            l = (new ClientDao()).runQuery(Clients.class, "SELECT * from clients");
            //l.stream().forEach(System.out::println);

        } catch (SQLException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        VRPTW v = new VRPTW();
        v.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        v.setVisible(true);
    }

}