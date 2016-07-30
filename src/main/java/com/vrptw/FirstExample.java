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
            idStrLst.add("depot");        //no borrar

            int vehicleID = 1;
            String demands = "3900,15895,26550,19960,9000,200,75,7950,1290,3250,6000,300,250,2000,60,680,2220,100,100,20,20,25";   //sin depot
            idStrLst.add("100061");
            idStrLst.add("100203");
            idStrLst.add("205747");
            idStrLst.add("203745");
            idStrLst.add("100581");
            idStrLst.add("100604");
            idStrLst.add("100679");
            idStrLst.add("201159");
            idStrLst.add("100357");
            idStrLst.add("207773");
            idStrLst.add("100692");
            idStrLst.add("100694");
            idStrLst.add("300001");
            idStrLst.add("100269");
            idStrLst.add("100261");
            idStrLst.add("300003");
            idStrLst.add("100425");
            idStrLst.add("100373");
            idStrLst.add("100374");
            idStrLst.add("100018");
            idStrLst.add("100271");
            idStrLst.add("100273");

            for(int i=0; i<idStrLst.size(); i++){
                    switch (idStrLst.get(i)) {
                        case "100017":  idStrLst.set(i,"100577"); System.out.printf("100017 -> 100577\n");break;
                        case "100086":  idStrLst.set(i,"100101"); System.out.printf("100086 -> 100101\n");break;
                        case "100102":  idStrLst.set(i,"207771"); System.out.printf("100102 -> 207771\n");break;
                        case "100103":  idStrLst.set(i,"100175"); System.out.printf("100103 -> 100175\n");break;
                        case "100166":  idStrLst.set(i,"100290"); System.out.printf("100166 -> 100290\n");break;
                        case "100198":  idStrLst.set(i,"100290"); System.out.printf("100198 -> 100290\n");break;
                        case "100273":  idStrLst.set(i,"100260"); System.out.printf("100273 -> 100260\n");break;
                        case "100296":  idStrLst.set(i,"200033"); System.out.printf("100296 -> 200033\n");break;
                        case "100374":  idStrLst.set(i,"100373"); System.out.printf("100374 -> 100373\n");break;
                        case "100409":  idStrLst.set(i,"100375"); System.out.printf("100409 -> 100375\n");break;
                        case "100413":  idStrLst.set(i,"100373"); System.out.printf("100413 -> 100373\n");break;
                        case "100422":  idStrLst.set(i,"100149"); System.out.printf("100422 -> 100149\n");break;
                        case "100499":  idStrLst.set(i,"100425"); System.out.printf("100499 -> 100425\n");break;
                        case "100537":  idStrLst.set(i,"100581"); System.out.printf("100537 -> 100581\n");break;
                        case "100562":  idStrLst.set(i,"100016"); System.out.printf("100562 -> 100016\n");break;
                        case "100588":  idStrLst.set(i,"100248"); System.out.printf("100588 -> 100248\n");break;
                        case "100604":  idStrLst.set(i,"200033"); System.out.printf("100604 -> 200033\n");break;
                        case "100615":  idStrLst.set(i,"100016"); System.out.printf("100615 -> 100016\n");break;
                        case "100619":  idStrLst.set(i,"100373"); System.out.printf("100619 -> 100373\n");break;
                        case "100625":  idStrLst.set(i,"100245"); System.out.printf("100625 -> 100245\n");break;
                        case "100630":  idStrLst.set(i,"100098"); System.out.printf("100630 -> 100098\n");break;
                        case "100632":  idStrLst.set(i,"100290"); System.out.printf("100632 -> 100290\n");break;
                        case "100633":  idStrLst.set(i,"100294"); System.out.printf("100633 -> 100294\n");break;
                        case "100634":  idStrLst.set(i,"100290"); System.out.printf("100634 -> 100290\n");break;
                        case "100635":  idStrLst.set(i,"100290"); System.out.printf("100635 -> 100290\n");break;
                        case "100636":  idStrLst.set(i,"100259"); System.out.printf("100636 -> 100259\n");break;
                        case "100656":  idStrLst.set(i,"100294"); System.out.printf("100656 -> 100294\n");break;
                        case "100676":  idStrLst.set(i,"100674"); System.out.printf("100676 -> 100674\n");break;
                        case "100677":  idStrLst.set(i,"100373"); System.out.printf("100677 -> 100373\n");break;
                        case "100679":  idStrLst.set(i,"207775"); System.out.printf("100679 -> 207775\n");break;
                        case "100694":  idStrLst.set(i,"100692"); System.out.printf("100694 -> 100692\n");break;
                        case "100732":  idStrLst.set(i,"207786"); System.out.printf("100732 -> 207786\n");break;
                        case "100733":  idStrLst.set(i,"100770"); System.out.printf("100733 -> 100770\n");break;
                        case "100739":  idStrLst.set(i,"100577"); System.out.printf("100739 -> 100577\n");break;
                        case "100756":  idStrLst.set(i,"100674"); System.out.printf("100756 -> 100674\n");break;
                        default: System.out.printf(idStrLst.get(i)+"\n"); break;
                    }
            }
            /*
            List<Location> locationList = new ArrayList<>();
            // get clients with orders from date A to date B
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                locationList.addAll((new OrdersDao()).getLocationList("2015-06-22", "2015-06-22"));
            } catch(Exception e){}
            locationList.stream().forEach(lc->idStrLst.add(lc.getIdLocation())); */

            double[][] mat_Dist = (new RouteCostDao()).getDistanceCostMatrixByLocation(idStrLst);

            int[] vCapacity = new int[] {100000, 7500, 15000, 15000, 1150, 400, 600, 600, 600, 600, 400, 1000, 600, 400, 400, 10000};

            System.out.printf("\n\n\n\n\n\n\nprivate static int nbCustomers = %d;\n",mat_Dist.length-1);

            System.out.printf("private static int nbVehicles = 1;\n",mat_Dist.length);

            if(vCapacity[vehicleID-1]>7500){
                System.out.printf("private static int servTime = 2700;\n");   //  30min = 1800secs
            }else{
                System.out.printf("private static int servTime = 1800;\n");   //  45min = 2700secs
            }

            System.out.printf("private static int[] qty = new int[] {0,"+demands+"};\n");

            System.out.printf("private static int[] vCap = new int[] {%d};\n", vCapacity[vehicleID-1]);

            System.out.printf("private static int[][] costs = new int[][]\n{");      //kilometers
            for (int i = 0; i < mat_Dist.length; i++) {
                System.out.printf("{");
                for (int j = 0; j < mat_Dist[i].length; j++) {
                    if (j==mat_Dist[i].length-1){
                        System.out.printf("%5.0f", mat_Dist[i][j]);
                    }else {
                    System.out.printf("%5.0f,", mat_Dist[i][j]);}
                }
                if (i==mat_Dist.length-1){
                    System.out.printf("}");}
                    else{
                    System.out.printf("},\n");}
            } System.out.printf("};\n");

            double[][] mat_Time = (new RouteCostDao()).getTimeCostMatrixByLocation(idStrLst);
            System.out.printf("private static int[][] travTime = new int[][]\n{");      //seconds
            for (int i = 0; i < mat_Time.length; i++) {
                System.out.printf("{");
                for (int j = 0; j < mat_Time[i].length; j++) {
                    if (j==mat_Time[i].length-1){
                        System.out.printf("%5.0f", mat_Time[i][j]);
                    }else {
                        System.out.printf("%5.0f,", mat_Time[i][j]);}
                }
                if (i==mat_Time.length-1){
                    System.out.printf("}");}
                else{
                    System.out.printf("},\n");}
            } System.out.printf("};\n");

            System.out.printf("private static int[][] tWin = new int[][]\n",mat_Dist.length);
            System.out.printf("{");
            for (int k = 0; k < mat_Dist.length; k++) {
                System.out.printf("{28800, 86400}");
                if (k < mat_Dist.length-1){System.out.printf(",");}
            } System.out.printf("};\n");

        } catch (SQLException /*| ParseException*/ e) {
            e.printStackTrace();
        }


    }

}

