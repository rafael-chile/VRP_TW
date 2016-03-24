package com.vrptw;

import com.vrptw.dao.LocationDao;
import com.vrptw.dao.VehicleDao;
import com.vrptw.entities.Vehicle;

import java.sql.SQLException;
import java.util.List;

public class FirstExample {


    @SuppressWarnings(value = "unchecked")
    public static void main(String[] args) {

        VehicleDao vehicleDao = new VehicleDao();
        LocationDao locationDao = new LocationDao();

        try {
            List<Vehicle> allVechicles = vehicleDao.getList();
            System.out.println("Complete vehicles resulset count:" + allVechicles.size());
            allVechicles.stream().forEach(System.out::println);

            /*List<Location> allocations = locationDao.getList("69");
            System.out.println("Complete locations resulset count:" + allocations.size());
            allocations.stream().forEach(System.out::println);
            */
            //System.out.println("Filtered vehicles resulset count:" + vechiclesFiltered.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Other example using a very generic DAO
       /* try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fromDate = "2015-03-16";
            String toDate = formatter.format(new Date());

            List l = (new OrderdsDao()).getList(fromDate, toDate);
            l.stream().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }*/

    }

}