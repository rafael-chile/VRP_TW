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
            int[] vCapacity = new int[] {27000, 7500, 15000, 15000, 1150, 400, 600, 600, 600, 600, 400, 1000, 600, 400, 400, 10000};


            /** Modify instance:    */
            String fileName = "ThirdSol_1.txt";
            int[] vehicleID = new int[] {14,15,7,12,2,16,3,4};   //if vehicleID = 0  means the all fleet
            String demands = "1210,4700,42,1243,3208,450,3150,4599,100";
            idStrLst.add("100044");
            idStrLst.add("100362");
            idStrLst.add("100091");
            idStrLst.add("100019");
            idStrLst.add("100099");
            idStrLst.add("100113");
            idStrLst.add("100121");
            idStrLst.add("207744");
            idStrLst.add("100510");


            /** For Constraint 3.1: Pairs vehicle-client    */
            for(int i=1; i<idStrLst.size(); i++){
               // System.out.println("i = "+ i+ " idStrLst.size() = "+idStrLst.size());
                switch (idStrLst.get(i)) {
                    case "depot": System.out.println("depot");break;
                    case "100154": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "207810": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100714": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100261": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100098": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100313": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100406": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100163": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100625": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100245": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100351": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "207744": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100303": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100362": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100044": System.out.println("solver.post(ICF.arithm(serve["+ i +"][2], \"=\", 100));\t2");break;
                    case "100006": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "200017": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "207797": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100096": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "200032": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100101": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100102": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100103": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100563": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100085": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100086": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100499": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100588": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100149": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "207786": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100299": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100371": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100512": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100314": System.out.println("solver.post(ICF.arithm(serve["+ i +"][5], \"=\", 100));\t5");break;
                    case "100737": System.out.println("solver.post(ICF.arithm(serve["+ i +"][7], \"=\", 100));\t7");break;
                    case "207771": System.out.println("solver.post(ICF.arithm(serve["+ i +"][7], \"=\", 100));\t7");break;
                    case "200033": System.out.println("solver.post(ICF.arithm(serve["+ i +"][8], \"=\", 100));\t8");break;
                    case "100237": System.out.println("solver.post(ICF.arithm(serve["+ i +"][8], \"=\", 100));\t8");break;
                    case "100290": System.out.println("solver.post(ICF.arithm(serve["+ i +"][8], \"=\", 100));\t8");break;
                    case "207775": System.out.println("solver.post(ICF.arithm(serve["+ i +"][11], \"=\", 100));\t11");break;
                    case "100510": System.out.println("solver.post(ICF.arithm(serve["+ i +"][15], \"=\", 100));\t15");break;
                    case "100091": System.out.println("solver.post(ICF.arithm(serve["+ i +"][15], \"=\", 100));\t15");break;
                    default: System.out.println("-");break;
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


            System.out.printf("\n\n\n\n\nprivate static String testID = \""+ fileName +"\";\n");
            System.out.printf("private static int nbCustomers = %d;\n",mat_Dist.length-1);
            System.out.printf("private static int nbVehicles = %d;\n",vehicleID.length);

            /**  Fix the servTime for each vehicle, as a vector*/

            String serv_vector = "private static int[] servTime =  new int[] {";
            String vCap_vector = "private static int[] vCap = new int[] {";
            String vehicleID_vector = "private static int[] vehicleID = new int[] {";
            for (int i = 0; i < vehicleID.length; i++) {
                if (vCapacity[vehicleID[i] - 1] > 7500) {
                    serv_vector += "2700";  // 45min = 2700secs
                } else {
                    serv_vector += "1800";  // 30min = 1800secs
                }
                vCap_vector += vCapacity[vehicleID[i] - 1];
                vehicleID_vector += vehicleID[i];
                if (i == vehicleID.length -1) {
                    serv_vector += "};\n";  vCap_vector += "};\n"; vehicleID_vector += "};\n";
                }
                else {
                    serv_vector +=", "; vCap_vector +=", "; vehicleID_vector +=", ";
                }
            } System.out.printf(vCap_vector); System.out.printf(vehicleID_vector);
            System.out.print(serv_vector);

            System.out.printf("private static int[] qty = new int[] {0,"+demands+"};\n");

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
            System.out.printf("{{21600, 86400},");      //depot is opened from 6am to 12pm
            for (int k = 0; k < mat_Dist.length-1; k++) {
                System.out.printf("{28800, 72000}");    //locations are aopened in a wide TW from 8am to 8pm
                if (k < mat_Dist.length-2){System.out.printf(",");}
            } System.out.printf("};\n");

        } catch (SQLException /*| ParseException*/ e) {
            e.printStackTrace();
        }
    }
}

