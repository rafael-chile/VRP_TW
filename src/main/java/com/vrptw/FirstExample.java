package com.vrptw;

import com.vrptw.dao.*;
import com.vrptw.entities.Location;
import com.vrptw.entities.Orders;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FirstExample {


    @SuppressWarnings(value = "unchecked")
    public static void main(String[] args) {

        VehicleDao vehicleDao = new VehicleDao();
        LocationDao locationDao = new LocationDao();
        RouteCostDao routeCostDao = new RouteCostDao();
        ClientDao clientDao = new ClientDao();

        try {
            /*List<Vehicle> allVechicles = vehicleDao.getList();
            System.out.println("Complete vehicles resulset count:" + allVechicles.size());
            allVechicles.stream().forEach(System.out::println);
            */

            /*List<Location> allocations = locationDao.getList("69");
            System.out.println("Complete locations resulset count:" + allocations.size());
            allocations.stream().forEach(System.out::println);
            */

            /*List<Client> allocations = clientDao.getList("0"); // client0 -> DEPOT
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Client> allocationsStr = clientDao.getListIDsBetweenDates(sdf.parse("2015-06-22"), sdf.parse("2015-06-27"));
            allocations.stream().forEach(System.out::println);
            allocationsStr.stream().forEach(System.out::println);
            */
            //System.out.println("Found Client Ids:" + allocations.size());


            /*List<RouteCost> allRouteCost = routeCostDao.getList("2844");
            System.out.println("Complete RouteCost resulset count:" + allRouteCost.size());
            allRouteCost.stream().forEach(System.out::println);
            try {
                int res = routeCostDao.runInsertQuery("INSERT INTO route_costs (idclient_from,idclient_to,time_cost,distance_cost) " +
                        "VALUES ( \"XXXXXXX\", \"1815\", 2103.0, 23.895);");

                System.out.println("Inserted:" + res);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            */

            //System.out.println("Filtered vehicles resulset count:" + vechiclesFiltered.size());
        } catch (Exception e) {
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

        try {
            /*
            List<String> s = new ArrayList<>();
            s.add("2901");
            s.add("??29??");
            s.add("29??");
            s.add("0");
            s.add("10");
            s.stream().forEach(System.out::println);
            s.sort(String::compareTo);
            s.stream().forEach(System.out::println);

            List<String> clientsId = new ArrayList<>();
            clientsId.add("0");
            clientsId.add("6010");
            clientsId.add("4459");
            clientsId.add("7964");
            clientsId.add("7717");
            clientsId.add("822?3");

            //double [][] cost = routeCostDao.getDistanceCostMatrix(clientsId);
            double [][] cost = routeCostDao.getTimeCostMatrix(clientsId);

            System.out.print("\nPRINTING COSTS MATRIX:\n     ");
            clientsId.stream().forEach(id-> System.out.print( "| " + (id +"           ").substring(0,5) ));
            System.out.print("\n_________________________________________________\n");
            for(int i=0; i<cost[0].length; i++){
                System.out.print( (clientsId.get(i)+"           ").substring(0,5) +"| " );

                for(int j=0; j<cost[0].length; j++) {
                    System.out.print( (cost[i][j]+"                              ").substring(0,5) +"  " );
                }
                System.out.print( "\n" );
            }*/
/**/
         /*   List<Client> clientIdLst = new ArrayList<>();
            clientIdLst.add( (new ClientDao()).getList("0").get(0) ); // get the depot
            // get clients with orders from date A to date B
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
            clientIdLst.addAll( (new ClientDao()).getListIDsBetweenDates(sdf.parse("2015-06-22"),sdf.parse("2015-06-27")));
            } catch(Exception e){}
            List<String> clientsIds = new ArrayList<>();
            clientIdLst.stream().forEach(client->clientsIds.add(client.getIdClient()));
            //distnaces = routeCostDao.getDistanceCostMatrix(clientsIds);  */
            //TODO: times  = routeCostDao.getTimeCostMatrix(clientsIds);


            /*
            List<Orders> orderIdLst = new ArrayList<>();
            List<Location> locationList = new ArrayList<>();
            // get clients with orders from date A to date B
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                locationList.addAll((new OrdersDao()).getLocationList("2015-06-22", "2015-06-27"));
                orderIdLst.addAll((new OrdersDao()).getList("2015-06-22", "2015-06-27"));
            } catch(Exception e){}
            orderIdLst.stream().forEach(System.out::println);
            locationList.stream().forEach(System.out::println);*/
            List<String> idStrLst = new ArrayList<>();
            idStrLst.add("100019");
            idStrLst.add("100093");


            /*
            List<Location> locationList = new ArrayList<>();
            // get clients with orders from date A to date B
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                locationList.addAll((new OrdersDao()).getLocationList("2015-06-22", "2015-06-22"));
            } catch(Exception e){}
            locationList.stream().forEach(lc->idStrLst.add(lc.getIdLocation())); */
            double[][] mat_Dist = (new RouteCostDao()).getDistanceCostMatrixByLocation(idStrLst);
            for (int i = 0; i < mat_Dist.length; i++) {
                for (int j = 0; j < mat_Dist[i].length; j++) {
                    System.out.printf("%3.4f  ", mat_Dist[i][j]);
                }
                System.out.println();
            } System.out.println();

            double[][] mat_Time = (new RouteCostDao()).getTimeCostMatrixByLocation(idStrLst);
            for (int i = 0; i < mat_Time.length; i++) {
                for (int j = 0; j < mat_Time[i].length; j++) {
                    System.out.printf("%3.4f  ", mat_Time[i][j]);
                }
                System.out.println();
            }



        } catch (SQLException /*| ParseException*/ e) {
            e.printStackTrace();
        }


    }

}


